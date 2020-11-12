package vchat.state.api.queries

import cats.effect.IO
import vchat.state.models.values.{AccessToken, AccessTokenStatus}
import vchat.state.repositories.AccessTokenRepository

trait VerifyAccessToken {
  val tokenRepo: AccessTokenRepository

  def verifyAccessToken(token: AccessToken): IO[AccessTokenStatus] =
    tokenRepo.verify(token)
}
