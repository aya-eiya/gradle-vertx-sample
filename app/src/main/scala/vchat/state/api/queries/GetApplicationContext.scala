package vchat.state.api.queries

import vchat.state.models.ApplicationContext
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

trait GetApplicationContext {
  val appContextRepo: ApplicationContextRepository

  def getApplicationContext(
      accessToken: AccessToken
  ): Option[ApplicationContext] = appContextRepo.contextOf(accessToken)
}
