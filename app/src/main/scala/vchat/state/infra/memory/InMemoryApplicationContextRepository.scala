package vchat.state.infra.memory

import cats.data.OptionT
import cats.effect.IO
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.AuthNStatus
import vchat.state.models.{AccessContext, ApplicationContext, Context}
import vchat.state.models.values.{SessionID, TimeoutSessionIDStatus}
import vchat.state.repositories.ApplicationContextRepository

import scala.reflect.ClassTag
import collection.JavaConverters._
import scala.collection.mutable.{Map => MutMap}
import java.util.concurrent.{ConcurrentHashMap => JCMap}

object InMemoryApplicationContextRepository
    extends ApplicationContextRepository {
  private val data: MutMap[SessionID, ApplicationContext] =
    new JCMap[SessionID, ApplicationContext]().asScala

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
