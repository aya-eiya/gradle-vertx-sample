package vchat.state.api.impl

import vchat.state.api.ApplicationContextManager
import vchat.state.infra.memory.{
  InMemoryAccessTokenRepository,
  InMemoryApplicationContextRepository
}
import vchat.state.repositories.{
  AccessTokenRepository,
  ApplicationContextRepository
}

object StaticApplicationContextManager extends ApplicationContextManager {

  override val tokenRepo: AccessTokenRepository = InMemoryAccessTokenRepository
  override val appContextRepo: ApplicationContextRepository =
    InMemoryApplicationContextRepository
}
