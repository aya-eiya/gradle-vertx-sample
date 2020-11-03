package vchat.auth.infra.memory

import vchat.auth.domain.models.{ApplicationContext, AuthNStatus, LoginContext}
import vchat.auth.domain.repositories.ApplicationStateRepository
import vchat.state.models.values.AccessToken

import scala.collection.mutable.{Map => MutMap}

object InMemoryApplicationStateRepository extends ApplicationStateRepository {
  private val data = MutMap[AccessToken, ApplicationContext]()
  override def create(
      accessToken: AccessToken,
      authNStatus: AuthNStatus
  ): Unit =
    data.put(
      accessToken,
      ApplicationContext(LoginContext(accessToken, authNStatus))
    )
  override def contextOf(accessToken: AccessToken): Option[ApplicationContext] =
    data.get(accessToken)

}
