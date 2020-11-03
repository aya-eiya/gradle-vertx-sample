package vchat.auth.domain.repositories

import vchat.auth.domain.models.{ApplicationContext, AuthNStatus}
import vchat.state.models.values.AccessToken

trait ApplicationStateRepository {
  def create(accessToken: AccessToken, authNStatus: AuthNStatus): Unit
  def contextOf(accessToken: AccessToken): Option[ApplicationContext]
}
