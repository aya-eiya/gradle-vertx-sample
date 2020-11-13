package vchat.app.service.auth

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
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import vchat.app.service.auth.email.{
  AccessTokenDataFetcher,
  VerifyPasswordDataFetcher
}
import vchat.auth.api.EmailAuthorizer
import vchat.auth.api.impl.StaticEmailAuthorizer
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.email.{
  EmailAuthNErrorStatus,
  EmailAuthNStatus
}
import vchat.server.graphql.DataFetcherHandler
import vchat.server.{GraphQLMixIn, Service, UseGraphQLApplicationContext}
import vchat.state.api.ApplicationContextManager
import vchat.state.api.impl.StaticApplicationContextManager
import vchat.state.models.values.AccessToken

object EmailAuth extends email.GraphQLSchema with email.ErrorStatuses {
  def verticleName: String = nameForVerticle[EmailAuth]
}

class EmailAuth
    extends Service
    with GraphQLMixIn
    with UseGraphQLApplicationContext {
  import EmailAuth._
  import email.GraphQLSchema._

  def authorizer: EmailAuthorizer = StaticEmailAuthorizer
  def verifyPasswordDataFetcher: DataFetcherHandler[LoginStatusData] =
    VerifyPasswordDataFetcher(login)
  def accessTokenDataFetcher: DataFetcherHandler[String] =
    AccessTokenDataFetcher(
      getAvailableAccessToken
    )

  override def contextManager: ApplicationContextManager =
    StaticApplicationContextManager

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
              verifyPasswordDataFetcher.build
            )
            .dataFetcher(
              "accessToken",
              accessTokenDataFetcher.build
            )
      )
      .build
    GraphQL.newGraphQL(gen.makeExecutableSchema(reg, wiring)).build
  }

  def getAvailableAccessToken(
      env: DataFetchingEnvironment
  ): IO[AccessToken] =
    getToken(env).getOrElseF(createToken)

  private def login(
      env: DataFetchingEnvironment
  ): EitherT[IO, EmailAuthNErrorStatus, LoginStatusData] =
    for {
      d <- getInputValues(env)
      a <- verifyInput(d)
      t <- getToken(env).toRight(invalidAccessTokenStatus)
      c <- verifyPassword(t, a)
      _ <- setContext(t, c)
    } yield LoginStatusData(t.value, c.token.base64)

  private def verifyPassword(
      accessToken: AccessToken,
      input: EmailAuthInput
  ): EitherT[IO, EmailAuthNErrorStatus, EmailAuthNStatus] =
    authorizer.verifyPassword(
      accessToken,
      input.emailAddress,
      input.rawPassword
    )

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
        failedToSetContextStatus
      )

}
