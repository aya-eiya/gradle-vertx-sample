package vchat.state.repositories

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.{AccessContext, ApplicationContext, Context}
import vchat.state.models.values.{
  AccessToken,
  AccessTokenStatus,
  TimeoutAccessTokenStatus
}
import vchat.utilities.time.AppTime

import scala.reflect.ClassTag

private object ApplicationContextRepository extends AppTime {
  def resetTimeout(
      accessTokenStatus: AccessTokenStatus
  ): IO[AccessTokenStatus] =
    if (accessTokenStatus.exists) IO(accessTokenStatus)
    else
      IO(
        TimeoutAccessTokenStatus(
          currentTimeMillis + defaultTokenTimeout
        )
      )
}

trait ApplicationContextRepository {
  import ApplicationContextRepository._
  def create(accessToken: AccessToken): IO[Unit]
  def contextOf(accessToken: AccessToken): OptionT[IO, ApplicationContext]

  def putContext[T <: Context: ClassTag](
      accessToken: AccessToken,
      context: T
  ): OptionT[IO, Unit]

  def resetAccessTokenTimeout(accessToken: AccessToken): OptionT[IO, Unit] =
    for {
      a <- contextOf(accessToken)
      b =
        a.childContexts
          .find(_.isInstanceOf[AccessContext])
          .map(_.asInstanceOf[AccessContext])
      c <- OptionT(IO(b))
      d <- OptionT(resetTimeout(c.status).option)
    } yield putContext[AccessContext](accessToken, AccessContext(d))
}
