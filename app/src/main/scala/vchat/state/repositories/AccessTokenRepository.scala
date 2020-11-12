package vchat.state.repositories

import cats.effect.IO
import vchat.state.models.values.{AccessToken, AccessTokenStatus}

trait AccessTokenRepository {
  def create(): IO[AccessToken]
  def verify(token: AccessToken): IO[AccessTokenStatus]
}
