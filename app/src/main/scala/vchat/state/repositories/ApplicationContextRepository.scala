package vchat.state.repositories

import vchat.state.models.{AccessContext, ApplicationContext, Context}
import vchat.state.models.values.{
  AccessToken,
  AccessTokenStatus,
  TimeoutAccessTokenStatus
}
import vchat.utilities.time.AppTime

import scala.reflect.ClassTag

private object ApplicationContextRepository extends AppTime {
  def resetTimeout(accessTokenStatus: AccessTokenStatus): AccessTokenStatus =
    if (accessTokenStatus.exists) accessTokenStatus
    else
      TimeoutAccessTokenStatus(
        currentTimeMillis + defaultTokenTimeout
      )
}

trait ApplicationContextRepository {
  import ApplicationContextRepository._
  def create(accessToken: AccessToken): Unit
  def contextOf(accessToken: AccessToken): Option[ApplicationContext]

  def putContext[T <: Context: ClassTag](
      accessToken: AccessToken,
      context: T
  ): Unit

  def resetAccessTokenTimeout(accessToken: AccessToken): Unit =
    for {
      a <- contextOf(accessToken)
      b <-
        a.childContexts
          .find(_.isInstanceOf[AccessContext])
          .map(_.asInstanceOf[AccessContext])
    } yield putContext(
      accessToken,
      AccessContext(resetTimeout(b.status))
    )
}
