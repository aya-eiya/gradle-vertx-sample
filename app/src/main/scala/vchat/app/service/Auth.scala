package vchat.app.service

import graphql.GraphQL
import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.{
  SchemaGenerator,
  SchemaParser,
  TypeDefinitionRegistry,
  TypeRuntimeWiring
}
import io.vertx.ext.web.handler.graphql.VertxDataFetcher
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.ext.web.RoutingContext
import vchat.app.service.base._
import vchat.auth.api.EmailAuthorizer
import vchat.auth.api.impl.StaticEmailAuthorizer
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus,
  EmailAuthNStatus
}
import vchat.logging.ErrorDescription
import vchat.state.api.ApplicationContextManager
import vchat.state.api.impl.StaticApplicationContextManager
import vchat.state.models.AccessContext
import vchat.state.models.values.AccessToken

case class EmailAuthInput(
    rawEmailAddress: String,
    rawPassword: String
) {
  def emailAddress: AuthEmailAddress = AuthEmailAddress(rawEmailAddress)
}

case class Status(token: String)

private object ErrorDescriptions {
  def dataNotFound: ErrorDescription =
    ErrorDescription(
      reason = "送信データが取得できませんでした",
      todo = "データの送信方法が間違っている可能性があります",
      reference = ""
    )

  def emailAddressNotFound: ErrorDescription =
    ErrorDescription(
      reason = "emailAddressが入力されていません",
      todo = "emailAddressは必須です",
      reference = ""
    )
  def passwordNotFound: ErrorDescription =
    ErrorDescription(
      reason = "Passwordが入力されていません",
      todo = "Passwordは必須です",
      reference = ""
    )
  def emailAddressAndPasswordNotFound: ErrorDescription =
    ErrorDescription(
      reason = "emailAddressとpasswordが入力されていません",
      todo = "emailAddressとpasswordは必須です",
      reference = ""
    )

}

object Auth {
  type ResponseData = Either[EmailAuthNErrorStatus, Status]
  def accessTokenHeaderName = "Access-Token"
  def verticleName: String = nameForVerticle[Auth]
  def schema: String =
    """
      |type Status {
      |  token: String
      |}
      |
      |input EmailAuthInput {
      |  emailAddress: String
      |  rawPassword: String
      |}
      |
      |type Query {
      |  verifyPassword(input: EmailAuthInput): Status
      |}
      |""".stripMargin
}

class Auth extends Service with GraphQLMixIn {
  import Auth._
  import scala.collection.JavaConverters._

  def authorizer: EmailAuthorizer = StaticEmailAuthorizer
  def contextManager: ApplicationContextManager =
    StaticApplicationContextManager

  private def verifyPassword(
      accessToken: AccessToken,
      input: EmailAuthInput
  ): Either[EmailAuthNErrorStatus, EmailAuthNStatus] =
    authorizer.verifyPassword(
      accessToken,
      input.emailAddress,
      input.rawPassword
    )

  private def dataFetch(
      env: DataFetchingEnvironment
  ): ResponseData = {
    for {
      d <- getInputValues(env)
      a <- (d.get("emailAddress"), d.get("rawPassword")) match {
        case (Some(address), Some(pass)) =>
          Right(EmailAuthInput(address, pass))
        case (Some(_), None) =>
          Left(
            EmailAuthNErrorStatus(
              EmailAuthNErrorStatus.memberNotFound,
              ErrorDescriptions.emailAddressNotFound
            )
          )
        case (None, Some(_)) =>
          Left(
            EmailAuthNErrorStatus(
              EmailAuthNErrorStatus.memberNotFound,
              ErrorDescriptions.passwordNotFound
            )
          )
        case (None, None) =>
          Left(
            EmailAuthNErrorStatus(
              EmailAuthNErrorStatus.memberNotFound,
              ErrorDescriptions.emailAddressAndPasswordNotFound
            )
          )
      }
      t = getToken(env).getOrElse(createToken)
      c <- verifyPassword(t, a)
    } yield Status(c.token.toString)
  }

  private def getInputValues(
      env: DataFetchingEnvironment
  ): Either[EmailAuthNErrorStatus, Map[String, String]] =
    Option(env.getArgument[java.util.Map[String, String]]("input"))
      .toRight(
        EmailAuthNErrorStatus(
          EmailAuthNErrorStatus.wrongEmailAddressErrorCode,
          ErrorDescriptions.dataNotFound
        )
      )
      .map(_.asScala.toMap)

  private def createToken: AccessToken = {
    val newToken = contextManager.createAccessToken
    contextManager.createApplicationContext(newToken)
    newToken
  }

  private def getToken(
      env: DataFetchingEnvironment
  ): Option[AccessToken] =
    for {
      c <-
        env
          .getContext[RoutingContext]()
          .request()
          .headers()
          .get(accessTokenHeaderName)
      _ = println(s"c:$c")
      t = AccessToken(c)
      _ = println(s"t:$t")
      v <- contextManager.getApplicationContext(t)
      _ = println(s"v:$v")
      o <- v.get[AccessContext]
      _ = println(s"o:$o")
    } yield t

  override def graphQLHandler: GraphQL = {
    val parser = new SchemaParser
    val reg: TypeDefinitionRegistry = parser.parse(schema)
    val gen = new SchemaGenerator

    val vdf =
      new VertxDataFetcher[Status]((env, p) =>
        dataFetch(env) match {
          case Right(status) => p.complete(status)
          case Left(err)     => p.fail(err.toString)
        }
      )
    val wiring = newRuntimeWiring
      .`type`(
        "Query",
        (builder: TypeRuntimeWiring.Builder) =>
          builder.dataFetcher(
            "verifyPassword",
            vdf
          )
      )
      .build
    GraphQL.newGraphQL(gen.makeExecutableSchema(reg, wiring)).build()
  }
}
