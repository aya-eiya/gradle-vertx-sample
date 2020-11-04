package vchat.state.api.queries

import vchat.state.models.values.AccessToken
import vchat.state.repositories.AccessTokenRepository

trait GetAccessToken {
  val tokenRepo: AccessTokenRepository

  def getAccessToken: Option[AccessToken] = tokenRepo.get()

  def getOrCreateAccessToken: AccessToken = tokenRepo.getOrCreate()
}
