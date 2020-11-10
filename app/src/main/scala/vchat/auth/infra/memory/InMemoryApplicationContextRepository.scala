package vchat.auth.infra.memory

import vchat.auth.domain.models.values.AuthNStatus
import vchat.auth.domain.models.LoginContext
import vchat.state.models.ApplicationContext
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

import scala.collection.mutable.{Map => MutMap}

object InMemoryApplicationContextRepository
    extends ApplicationContextRepository {
  private val data = MutMap[AccessToken, ApplicationContext]()

  override def contextOf(accessToken: AccessToken): Option[ApplicationContext] =
    data.get(accessToken)

  override def create(accessToken: AccessToken): Unit =
    data.put(
      accessToken,
      ApplicationContext(
        Seq(
          LoginContext(
            accessToken,
            AuthNStatus
              .empty(accessToken)
          )
        )
      )
    )
}
