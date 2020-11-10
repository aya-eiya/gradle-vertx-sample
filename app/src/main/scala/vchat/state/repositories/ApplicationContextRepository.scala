package vchat.state.repositories

import vchat.state.models.{AccessContext, ApplicationContext}
import vchat.state.models.values.{
  AccessToken,
  AccessTokenStatus,
  TimeoutAccessTokenStatus
}
import vchat.utilities.time.AppTime

private object ApplicationContextRepository extends AppTime {
  def resetTimeout(accessTokenStatus: AccessTokenStatus): AccessTokenStatus =
    if (accessTokenStatus.exists) accessTokenStatus
    else
      TimeoutAccessTokenStatus(
        currentTimeMillis + defaultTokenTimeout
      )
}

trait ApplicationContextRepository {
  def create(accessToken: AccessToken): Unit
  def contextOf(accessToken: AccessToken): Option[ApplicationContext]
  def put(accessToken: AccessToken, accessTokenStatus: AccessTokenStatus): Unit
  def resetAccessTokenTimeout(accessToken: AccessToken): Unit =
    for {
      a <- contextOf(accessToken)
      b <-
        a.childContexts
          .find(_.isInstanceOf[AccessContext])
          .map(_.asInstanceOf[AccessContext])
    } yield put(
      accessToken,
      ApplicationContextRepository.resetTimeout(b.status)
    )
}
