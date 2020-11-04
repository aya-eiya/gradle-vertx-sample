package vchat.state.api.impl

import vchat.state.api.commands.CreateAccessToken
import vchat.state.api.queries.{GetAccessToken, VerifyAccessToken}
import vchat.state.infra.memory.InMemoryAccessTokenRepository
import vchat.state.models.values.AccessToken
import vchat.state.repositories.AccessTokenRepository

object StaticAccessTokenStore
    extends CreateAccessToken
    with GetAccessToken
    with VerifyAccessToken {

  override val tokenRepo: AccessTokenRepository = InMemoryAccessTokenRepository
}
