package vchat.state.api.commands

import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

trait ResetAccessTokenTimeout {

  val appContextRepo: ApplicationContextRepository
  def resetAccessTokenTimeout(accessToken: AccessToken): Unit =
    appContextRepo.resetAccessTokenTimeout(accessToken)
}
