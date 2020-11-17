package vchat.state.api.commands

import cats.effect.IO
import vchat.state.models.values.SessionID
import vchat.state.repositories.ApplicationContextRepository

trait CreateApplicationContext {

  val appContextRepo: ApplicationContextRepository
  def createApplicationContext(sessionID: SessionID): IO[Unit] =
    appContextRepo.create(sessionID)
}
