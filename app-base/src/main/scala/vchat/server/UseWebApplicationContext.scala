package vchat.server

import cats.data.OptionT
import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import io.vertx.scala.ext.web.RoutingContext
import vchat.state.api.ApplicationContextManager
import vchat.state.models.AccessContext
import vchat.state.models.values.SessionID

object UseWebApplicationContext {
  def sessionIDHeaderName = "Session-Id"
}

trait UseWebApplicationContext extends LazyLogging {
  import logger._
  import UseWebApplicationContext._
  def contextManager: ApplicationContextManager

  def createSessionId: IO[SessionID] =
    for {
      newToken <- contextManager.createSessionID
      _ <- contextManager.createApplicationContext(newToken)
    } yield newToken

  def getSessionID(
      context: RoutingContext
  ): OptionT[IO, SessionID] =
    for {
      c <- getSessionIdFromHeader(context)
      t = SessionID(c)
      _ = info(s"SessionID(header):$t")
      v <- contextManager.getApplicationContext(t)
      _ = info(s"ApplicationContext of $t:$v")
      _ <- v.get[AccessContext]
    } yield t

  private def getSessionIdFromHeader(
      context: RoutingContext
  ): OptionT[IO, String] =
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
