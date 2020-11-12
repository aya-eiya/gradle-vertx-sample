package vchat.state.infra.memory

import cats.data.OptionT
import cats.effect.IO
import cats.effect.implicits._
import vchat.state.models.values.{
  AccessToken,
  AccessTokenStatus,
  TimeoutAccessTokenStatus
}
import vchat.state.repositories.AccessTokenRepository

import scala.collection.mutable.{Map => MutMap}

object InMemoryAccessTokenRepository extends AccessTokenRepository {
  private val data = MutMap[AccessToken, AccessTokenStatus]()

  override def create(): IO[AccessToken] = {
    val token = AccessToken(s"testToken_${data.size}")
    data.put(
      token,
      TimeoutAccessTokenStatus.create
    )
    IO(token)
  }

  override def verify(token: AccessToken): IO[AccessTokenStatus] =
    IO(data.getOrElse(token, AccessTokenStatus.NotExists))

}
