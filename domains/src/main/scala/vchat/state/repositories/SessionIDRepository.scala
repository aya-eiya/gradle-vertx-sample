package vchat.state.repositories

import cats.effect.IO
import vchat.state.models.values.{SessionID, SessionIDStatus}

trait SessionIDRepository {
  def create(): IO[SessionID]
  def verify(sessionID: SessionID): IO[SessionIDStatus]
}
