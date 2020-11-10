package vchat.state.infra.memory

import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.AuthNStatus
import vchat.state.models.{AccessContext, ApplicationContext}
import vchat.state.models.values.{
  AccessToken,
  AccessTokenStatus,
  TimeoutAccessTokenStatus
}
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
          AccessContext(TimeoutAccessTokenStatus.create),
          LoginContext(
            accessToken,
            AuthNStatus
              .empty(accessToken)
          )
        )
      )
    )

  override def put(
      accessToken: AccessToken,
      accessTokenStatus: AccessTokenStatus
  ): Unit =
    for {
      d <- data.get(accessToken)
      c <- d.get[AccessContext]
    } yield data.put(
      accessToken,
      d.put(AccessContext(c.status))
    )
}
