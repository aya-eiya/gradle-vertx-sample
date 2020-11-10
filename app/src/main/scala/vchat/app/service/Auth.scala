package vchat.app.service

import java.util

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
import vchat.app.service.base._
import vchat.auth.api.impl.StaticEmailAuthorizer
import vchat.auth.domain.models.EmailAuthorizer
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus,
  EmailAuthNStatus
}
import vchat.logging.ErrorDescription

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

  private def verifyPassword(
      input: EmailAuthInput
  ): Either[EmailAuthNErrorStatus, EmailAuthNStatus] =
    authorizer.verifyPassword(input.emailAddress, input.rawPassword)

  private def dataFetch(
      env: DataFetchingEnvironment
  ): ResponseData =
    for {
      u <- getInputValues(env).toRight(
        EmailAuthNErrorStatus(
          EmailAuthNErrorStatus.wrongEmailAddressErrorCode,
          ErrorDescriptions.dataNotFound
        )
      )
      d = u.asScala
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
      _ = println(s"input:$a")
      c <- verifyPassword(a)
      _ = println(s"verify:$c")
    } yield Status(c.token.toString)

  private def getInputValues(env: DataFetchingEnvironment) = {
    Option(env.getArgument[util.Map[String, String]]("input"))
  }

  override def graphQLHandler: GraphQL = {
    val parser = new SchemaParser()
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
