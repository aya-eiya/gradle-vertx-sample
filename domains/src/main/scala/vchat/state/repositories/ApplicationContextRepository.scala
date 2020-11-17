package vchat.state.repositories

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.{AccessContext, ApplicationContext, Context}
import vchat.state.models.values.{SessionID, SessionIDStatus}

import scala.reflect.ClassTag

trait ApplicationContextRepository {
  def create(sessionID: SessionID): IO[Unit]
  def contextOf(sessionID: SessionID): OptionT[IO, ApplicationContext]
  def resetTimeout(
      sessionIDStatus: SessionIDStatus
  ): IO[SessionIDStatus]
  def putContext[T <: Context: ClassTag](
      sessionID: SessionID,
      context: T
  ): OptionT[IO, Unit]

  def resetSessionIDTimeout(sessionID: SessionID): OptionT[IO, Unit] =
    for {
      a <- contextOf(sessionID)
      b =
        a.childContexts
          .find(_.isInstanceOf[AccessContext])
          .map(_.asInstanceOf[AccessContext])
      c <- OptionT(IO(b))
      d <- OptionT(resetTimeout(c.status).option)
    } yield putContext[AccessContext](sessionID, AccessContext(d))
}
