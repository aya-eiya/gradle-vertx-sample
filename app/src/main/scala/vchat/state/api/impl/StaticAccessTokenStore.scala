package vchat.state.api.impl

import vchat.state.api.commands.CreateAccessToken
import vchat.state.infra.memory.InMemoryAccessTokenRepository
import vchat.state.repositories.AccessTokenRepository

object StaticAccessTokenStore extends CreateAccessToken {
  override val tokenRepo: AccessTokenRepository = InMemoryAccessTokenRepository
}
