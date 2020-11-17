package vchat.auth.infra.memory

import java.util.concurrent.{ConcurrentHashMap => JCMap}

import cats.data.OptionT
import cats.effect.IO
import vchat.app.env.AppTime
import vchat.auth.models.LoginContext
import vchat.auth.models.values.AuthNStatus
import vchat.state.models.values.{
  SessionID,
  SessionIDStatus,
  TimeoutSessionIDStatus
}
import vchat.state.models.{AccessContext, ApplicationContext, Context}
import vchat.state.repositories.ApplicationContextRepository
import vchat.time.TimeScale

import scala.collection.JavaConverters._
import scala.collection.mutable.{Map => MutMap}
import scala.reflect.ClassTag

object InMemoryApplicationContextRepository
    extends ApplicationContextRepository
    with AppTime {
  private val data: MutMap[SessionID, ApplicationContext] =
    new JCMap[SessionID, ApplicationContext]().asScala

  override def resetTimeout(
      sessionIDStatus: SessionIDStatus
  ): IO[SessionIDStatus] =
    IO(
      TimeoutSessionIDStatus(TimeScale.default, defaultTokenTimeout)
    )

  override def contextOf(
      sessionID: SessionID
  ): OptionT[IO, ApplicationContext] =
    OptionT(IO(data.get(sessionID)))

  override def create(sessionID: SessionID): IO[Unit] =
    for {
      t <- IO(sessionID)
      a = AccessContext(TimeoutSessionIDStatus.create)
      l = LoginContext(t, AuthNStatus.empty(t))
      c = ApplicationContext(Seq(a, l))
    } yield data.put(t, c)

  override def putContext[T <: Context: ClassTag](
      sessionID: SessionID,
      context: T
  ): OptionT[IO, Unit] =
    for {
      b <- contextOf(sessionID)
      d <- OptionT.liftF(b.putContext(context))
    } yield data.put(sessionID, d)
}
