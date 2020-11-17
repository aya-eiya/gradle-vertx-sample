package vchat.auth.infra.memory

import vchat.state.api.ApplicationContextManager
import vchat.state.repositories.{
  ApplicationContextRepository,
  SessionIDRepository
}

object InMemoryApplicationContextManager extends ApplicationContextManager {

  override val tokenRepo: SessionIDRepository = InMemorySessionIDRepository
  override val appContextRepo: ApplicationContextRepository =
    InMemoryApplicationContextRepository
}
