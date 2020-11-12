package vchat.app.service

import cats.data.EitherT
import cats.effect.IO
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
import vchat.auth.api.EmailAuthorizer
import vchat.auth.api.impl.StaticEmailAuthorizer
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus,
  EmailAuthNStatus
}
import vchat.logging.ErrorDescription
import vchat.state.api.ApplicationContextManager
import vchat.state.api.impl.StaticApplicationContextManager
import vchat.state.models.values.AccessToken

case class EmailAuthInput(
    rawEmailAddress: String,
    rawPassword: String
) {
  def emailAddress: AuthEmailAddress = AuthEmailAddress(rawEmailAddress)
}

case class LoginStatusData(accessToken: String, authToken: String)

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
  def invalidAccessToken: ErrorDescription =
    ErrorDescription(
      reason = "accessTokenが正しくありません",
      todo = "Access-Tokenヘッダーに有効なトークンを指定してください",
      reference = ""
    )
  def failedToSetContext: ErrorDescription =
    ErrorDescription(
      reason = "情報の保存に失敗しました",
      todo = "システムの管理者に連絡してください",
      reference = ""
    )

}

object EmailAuth {
  type ResponseData = EitherT[IO, EmailAuthNErrorStatus, LoginStatusData]
  def verticleName: String = nameForVerticle[EmailAuth]
  def schema: String =
    """
      |type Status {
      |  accessToken: String!
      |  authToken: String
      |}
      |
      |input EmailAuthInput {
      |  emailAddress: String!
      |  rawPassword: String!
      |}
      |
      |type Query {
      |  accessToken: String
      |  verifyPassword(input: EmailAuthInput): Status
      |}
      |""".stripMargin
}

class EmailAuth
    extends Service
    with GraphQLMixIn
    with UseGraphQLApplicationContext {
  import EmailAuth._
  import scala.collection.JavaConverters._

  def authorizer: EmailAuthorizer = StaticEmailAuthorizer
  override def contextManager: ApplicationContextManager =
    StaticApplicationContextManager

  private def verifyPassword(
      accessToken: AccessToken,
      input: EmailAuthInput
  ): EitherT[IO, EmailAuthNErrorStatus, EmailAuthNStatus] =
    authorizer.verifyPassword(
      accessToken,
      input.emailAddress,
      input.rawPassword
    )

  private def verifyInputT(
      inputMap: Map[String, String]
  ): EitherT[IO, EmailAuthNErrorStatus, EmailAuthInput] =
    EitherT.fromEither(verifyInput(inputMap))
  private def verifyInput(
      inputMap: Map[String, String]
  ): Either[EmailAuthNErrorStatus, EmailAuthInput] =
    (inputMap.get("emailAddress"), inputMap.get("rawPassword")) match {
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

  private def login(
      env: DataFetchingEnvironment
  ): ResponseData =
    for {
      d <- getInputValues(env)
      a <- verifyInputT(d)
      t <- getToken(env).toRight(
        EmailAuthNErrorStatus(
          EmailAuthNErrorStatus.invalidAccessTokenErrorCode,
          ErrorDescriptions.emailAddressAndPasswordNotFound
        )
      )
      c <- verifyPassword(t, a)
      _ <- setContext(t, c)
    } yield LoginStatusData(t.value, c.token.base64)

  private def setContext(
      token: AccessToken,
      status: EmailAuthNStatus
  ): EitherT[IO, EmailAuthNErrorStatus, Unit] =
    contextManager
      .setContext(
        token,
        LoginContext(token, status)
      )
      .toRight(
        EmailAuthNErrorStatus(
          EmailAuthNErrorStatus.systemErrorCode,
          ErrorDescriptions.failedToSetContext
        )
      )

  private def accessToken(env: DataFetchingEnvironment): IO[AccessToken] =
    getToken(env).getOrElseF(createToken)

  def verifyPasswordDataFetcher: VertxDataFetcher[LoginStatusData] =
    new VertxDataFetcher[LoginStatusData]((env, p) =>
      login(env)
        .transform {
          case Right(status) => Right(p.complete(status))
          case Left(err)     => Left(p.fail(err.toString))
        }
        .value
        .unsafeRunAsync {
          case Right(value)    => println(value)
          case Left(throwable) => println(throwable)
        }
    )

  def accessTokenDataFetcher: VertxDataFetcher[String] =
    new VertxDataFetcher[String]((env, p) =>
      (for {
        t <- accessToken(env)
      } yield p
        .complete(t.value))
        .unsafeRunAsync {
          case Right(value)    => println(value)
          case Left(throwable) => println(throwable)
        }
    )

  private def getInputValues(
      env: DataFetchingEnvironment
  ): EitherT[IO, EmailAuthNErrorStatus, Map[String, String]] =
    EitherT(
      IO(
        Option(env.getArgument[java.util.Map[String, String]]("input"))
          .toRight(
            EmailAuthNErrorStatus(
              EmailAuthNErrorStatus.wrongEmailAddressErrorCode,
              ErrorDescriptions.dataNotFound
            )
          )
          .map(_.asScala.toMap)
      )
    )

  override def graphQLHandler: GraphQL = {
    val parser = new SchemaParser
    val reg: TypeDefinitionRegistry = parser.parse(schema)
    val gen = new SchemaGenerator
    val wiring = newRuntimeWiring
      .`type`(
        "Query",
        (builder: TypeRuntimeWiring.Builder) =>
          builder
            .dataFetcher(
              "verifyPassword",
              verifyPasswordDataFetcher
            )
            .dataFetcher(
              "accessToken",
              accessTokenDataFetcher
            )
      )
      .build
    GraphQL.newGraphQL(gen.makeExecutableSchema(reg, wiring)).build()
  }

}
