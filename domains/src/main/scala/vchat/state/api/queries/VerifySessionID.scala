package vchat.state.api.queries

import cats.effect.IO
import vchat.state.models.values.{SessionID, SessionIDStatus}
import vchat.state.repositories.SessionIDRepository

trait VerifySessionID {
  val tokenRepo: SessionIDRepository

  def verifySessionID(token: SessionID): IO[SessionIDStatus] =
    tokenRepo.verify(token)
}
