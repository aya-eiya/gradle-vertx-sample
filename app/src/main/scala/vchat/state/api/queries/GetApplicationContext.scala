package vchat.state.api.queries

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.ApplicationContext
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

trait GetApplicationContext {
  val appContextRepo: ApplicationContextRepository

  def getApplicationContext(
      accessToken: AccessToken
  ): OptionT[IO, ApplicationContext] = appContextRepo.contextOf(accessToken)
}
