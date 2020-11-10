package vchat.state.api.commands

import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

trait CreateApplicationContext {

  val appContextRepo: ApplicationContextRepository
  def createApplicationContext(accessToken: AccessToken): Unit =
    appContextRepo.create(accessToken)
}
