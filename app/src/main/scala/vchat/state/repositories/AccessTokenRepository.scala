package vchat.state.repositories

import vchat.state.models.values.{AccessToken, AccessTokenStatus}

trait AccessTokenRepository {
  def create(): AccessToken
  def verify(token: AccessToken): AccessTokenStatus
}
