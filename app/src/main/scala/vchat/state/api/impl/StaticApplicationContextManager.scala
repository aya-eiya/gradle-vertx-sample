package vchat.state.api.impl

import vchat.state.api.ApplicationContextManager
import vchat.state.infra.memory.{
  InMemorySessionIDRepository$,
  InMemoryApplicationContextRepository
}
import vchat.state.repositories.{
  SessionIDRepository,
  ApplicationContextRepository
}

object StaticApplicationContextManager extends ApplicationContextManager {

  override val tokenRepo: SessionIDRepository = InMemorySessionIDRepository$
  override val appContextRepo: ApplicationContextRepository =
    InMemoryApplicationContextRepository
}
