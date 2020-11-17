package vchat.auth.infra.memory

import java.util.concurrent.{ConcurrentHashMap => JCMap}

import cats.effect.IO
import vchat.state.models.values.{
  SessionID,
  SessionIDStatus,
  TimeoutSessionIDStatus
}
import vchat.state.repositories.SessionIDRepository

import scala.collection.JavaConverters._
import scala.collection.mutable.{Map => MutMap}

object InMemorySessionIDRepository extends SessionIDRepository {
  private val data: MutMap[SessionID, SessionIDStatus] =
    new JCMap[SessionID, SessionIDStatus]().asScala

  override def create(): IO[SessionID] =
    for {
      t <- IO(SessionID(s"testToken_${data.size}"))
      _ = data.put(t, TimeoutSessionIDStatus.create)
    } yield t

  override def verify(sessionID: SessionID): IO[SessionIDStatus] =
    IO(data.getOrElse(sessionID, SessionIDStatus.NotExists))
}
