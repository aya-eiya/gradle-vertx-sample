package vchat.server

import _root_.graphql.schema.DataFetchingEnvironment
import cats.data.OptionT
import cats.effect.IO
import io.vertx.scala.ext.web.RoutingContext
import vchat.state.api.ApplicationContextManager
import vchat.state.models.AccessContext
import vchat.state.models.values.SessionID

object UseWebApplicationContext {
  def sessionIDHeaderName = "Access-Token"
}

trait UseWebApplicationContext {
  import UseWebApplicationContext._
  def contextManager: ApplicationContextManager

  def createToken: IO[SessionID] =
    for {
      newToken <- contextManager.createSessionID
      _ <- contextManager.createApplicationContext(newToken)
    } yield newToken

  def getToken(
      context: RoutingContext
  ): OptionT[IO, SessionID] =
    for {
      c <- getHeaderToken(context)
      _ = println(s"a-c:$c")
      t = SessionID(c)
      _ = println(s"a-t:$t")
      v <- contextManager.getApplicationContext(t)
      _ = println(s"a-v:$v")
      _ <- v.get[AccessContext]
    } yield t

  private def getHeaderToken(context: RoutingContext) =
    OptionT(
      IO(
        context
          .request()
          .headers()
          .get(sessionIDHeaderName) match {
          case None | Some(null) => None
          case Some(token)       => Some(token)
        }
      )
    )
}

trait UseGraphQLApplicationContext extends UseWebApplicationContext {
  def getToken(
      env: DataFetchingEnvironment
  ): OptionT[IO, SessionID] = getToken(env.getContext[RoutingContext])
}
