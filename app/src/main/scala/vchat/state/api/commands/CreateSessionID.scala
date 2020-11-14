package vchat.state.api.commands

import cats.effect.IO
import vchat.state.models.values.SessionID
import vchat.state.repositories.SessionIDRepository

trait CreateSessionID {

  val tokenRepo: SessionIDRepository
  def createSessionID: IO[SessionID] = tokenRepo.create()
}
