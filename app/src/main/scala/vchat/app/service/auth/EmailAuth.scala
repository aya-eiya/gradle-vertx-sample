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
import vchat.auth.api.email.EmailAuthorizer
import vchat.auth.models.values.email.{EmailAuthNErrorStatus, EmailAuthNStatus}
import vchat.server.graphql.{
  DataFetcherHandler,
  GraphQLMixIn,
  UseGraphQLApplicationContext
}
import vchat.server.graphql.state.SessionIDDataFetcher
import vchat.server.Service
import vchat.state.api.ApplicationContextManager
import vchat.state.models.values.SessionID
import vchat.app.env.AppEnvMap
import vchat.app.service.AppService
import vchat.auth.models.LoginContext
import vchat.auth.infra.memory.{
  InMemoryApplicationContextManager,
  InMemoryEmailAuthorizer
}

object EmailAuth {
  def verticleName: String = nameForVerticle[EmailAuth]
}

class EmailAuth
    extends AppService
    with GraphQLMixIn
    with UseGraphQLApplicationContext {
  import logger._
  import ErrorStatuses._
  import GraphQLSchema._

  def authorizer: EmailAuthorizer = InMemoryEmailAuthorizer
  def verifyPasswordDataFetcher: DataFetcherHandler[LoginStatusData] =
    VerifyPasswordDataFetcher(login)
  def sessionIDDataFetcher: DataFetcherHandler[String] =
    SessionIDDataFetcher(
      getAvailableSessionID
    )

  override def contextManager: ApplicationContextManager =
    InMemoryApplicationContextManager

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
