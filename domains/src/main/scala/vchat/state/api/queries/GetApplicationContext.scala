package vchat.state.api.queries

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.ApplicationContext
import vchat.state.models.values.SessionID
import vchat.state.repositories.ApplicationContextRepository

trait GetApplicationContext {
  val appContextRepo: ApplicationContextRepository

  def getApplicationContext(
      sessionID: SessionID
  ): OptionT[IO, ApplicationContext] = appContextRepo.contextOf(sessionID)
}
