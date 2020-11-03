package vchat.auth.repositories

import vchat.auth.AuthNStatus
import vchat.state.AccessToken

case class LoginContext(
    accessToken: AccessToken,
    authNStatus: AuthNStatus
)
case class ApplicationContext(
    loginContext: LoginContext
)

trait ApplicationStateRepository {
  def create(accessToken: AccessToken, authNStatus: AuthNStatus): Unit
  def contextOf(accessToken: AccessToken): Option[ApplicationContext]
}
