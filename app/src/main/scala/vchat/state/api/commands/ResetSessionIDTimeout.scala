package vchat.state.api.commands

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.values.SessionID
import vchat.state.repositories.ApplicationContextRepository

trait ResetSessionIDTimeout {

  val appContextRepo: ApplicationContextRepository
  def resetSessionIDTimeout(sessionID: SessionID): OptionT[IO, Unit] =
    appContextRepo.resetSessionIDTimeout(sessionID)
}
