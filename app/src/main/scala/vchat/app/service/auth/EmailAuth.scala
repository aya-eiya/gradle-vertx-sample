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
import vchat.app.service.auth.email.VerifyPasswordDataFetcher
import vchat.app.service.auth.email.error.ErrorStatuses
import vchat.app.service.auth.email.schema.GraphQLSchema
import vchat.auth.api.EmailAuthorizer
import vchat.auth.api.impl.StaticEmailAuthorizer
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.email.{
  EmailAuthNErrorStatus,
  EmailAuthNStatus
}
import vchat.server.graphql.{DataFetcherHandler, UseGraphQLApplicationContext}
import vchat.server.graphql.state.SessionIDDataFetcher
import vchat.server.{GraphQLMixIn, Service}
import vchat.state.api.ApplicationContextManager
import vchat.state.api.impl.StaticApplicationContextManager
import vchat.state.models.values.SessionID

object EmailAuth extends GraphQLSchema with ErrorStatuses {
  def verticleName: String = nameForVerticle[EmailAuth]
}

class EmailAuth
    extends Service
    with GraphQLMixIn
    with UseGraphQLApplicationContext {
  import logger._
  import EmailAuth._
  import email.schema.GraphQLSchema._

  def authorizer: EmailAuthorizer = StaticEmailAuthorizer
  def verifyPasswordDataFetcher: DataFetcherHandler[LoginStatusData] =
    VerifyPasswordDataFetcher(login)
  def sessionIDDataFetcher: DataFetcherHandler[String] =
    SessionIDDataFetcher(
      getAvailableSessionID
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
              "sessionID",
              sessionIDDataFetcher.build
            )
      )
      .build
    GraphQL.newGraphQL(gen.makeExecutableSchema(reg, wiring)).build
  }

  def getAvailableSessionID(
      env: DataFetchingEnvironment
  ): IO[SessionID] =
    getSessionID(env).getOrElseF(createSessionId)

  private def login(
      env: DataFetchingEnvironment
  ): EitherT[IO, EmailAuthNErrorStatus, LoginStatusData] =
    for {
      d <- getInputValues(env)
      a <- verifyInput(d)
      t <- getSessionID(env).toRight(invalidSessionIDStatus)
      c <- verifyPassword(t, a)
      _ <- setContext(t, c)
      _ = info("login success")
    } yield LoginStatusData(t.value, c.token.base64)

  private def verifyPassword(
      sessionID: SessionID,
      input: EmailAuthInput
  ): EitherT[IO, EmailAuthNErrorStatus, EmailAuthNStatus] =
    authorizer.verifyPassword(
      sessionID,
      input.emailAddress,
      input.rawPassword
    )

  private def setContext(
      token: SessionID,
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
