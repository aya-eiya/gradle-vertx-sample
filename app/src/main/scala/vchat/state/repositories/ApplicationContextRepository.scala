package vchat.state.repositories

import vchat.state.models.ApplicationContext
import vchat.state.models.values.AccessToken

trait ApplicationContextRepository {
  def create(accessToken: AccessToken): Unit
  def contextOf(accessToken: AccessToken): Option[ApplicationContext]
}
