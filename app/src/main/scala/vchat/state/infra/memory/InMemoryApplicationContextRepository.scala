package vchat.state.infra.memory

import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.AuthNStatus
import vchat.state.models.{AccessContext, ApplicationContext, Context}
import vchat.state.models.values.{AccessToken, TimeoutAccessTokenStatus}
import vchat.state.repositories.ApplicationContextRepository

import scala.collection.mutable.{Map => MutMap}
import scala.reflect.ClassTag

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

  override def putContext[T <: Context: ClassTag](
      accessToken: AccessToken,
      context: T
  ): Unit =
    for {
      d <- data.get(accessToken)
      _ <- d.get[AccessContext]
    } yield data.put(
      accessToken,
      d.put(context)
    )
}
