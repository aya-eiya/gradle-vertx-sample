package vchat.state.api.queries

import vchat.state.models.values.{AccessToken, AccessTokenStatus}
import vchat.state.repositories.AccessTokenRepository

trait VerifyAccessToken {
  val tokenRepo: AccessTokenRepository

  def verifyAccessToken(token: AccessToken): AccessTokenStatus =
    tokenRepo.verify(token)
}
