package vchat.state.infra.memory

import vchat.state.models.values.{AccessToken, AccessTokenStatus}
import vchat.state.repositories.AccessTokenRepository

import scala.collection.mutable.{Map => MutMap}

object InMemoryAccessTokenRepository extends AccessTokenRepository {
  private val data = MutMap[AccessToken, AccessTokenStatus]()

  override def create(): AccessToken = {
    val token = AccessToken(s"testToken_${data.size}")
    data.put(
      token,
      AccessTokenStatus(exists = true, expired = true)
    )
    token
  }

  override def verify(token: AccessToken): AccessTokenStatus =
    data.getOrElse(token, AccessTokenStatus.NotExists)

  override def get(): Option[AccessToken] = Option.empty
}
