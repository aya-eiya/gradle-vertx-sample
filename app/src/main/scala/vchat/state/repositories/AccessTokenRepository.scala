package vchat.state.repositories

import vchat.state.models.values.{AccessToken, AccessTokenStatus}

trait AccessTokenRepository {
  def create(): AccessToken
  def get(): Option[AccessToken]
  def verify(token: AccessToken): AccessTokenStatus

  def getOrCreate(): AccessToken =
    get.filter(verify(_).existsAndNotExpired).getOrElse(create)
}
