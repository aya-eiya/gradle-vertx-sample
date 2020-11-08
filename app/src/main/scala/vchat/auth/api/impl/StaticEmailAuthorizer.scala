package vchat.auth.api.impl

import vchat.auth.domain.models.EmailAuthorizer
import vchat.auth.domain.repositories.{
  ApplicationStateRepository,
  MemberEmailRepository
}
import vchat.auth.infra.memory.{
  InMemoryApplicationStateRepository,
  InMemoryMemberEmailRepository
}
import vchat.state.api.impl.StaticAccessTokenStore
import vchat.state.models.values.AccessToken

object StaticEmailAuthorizer extends EmailAuthorizer {

  override val AuthorizerId: String = "StaticEmailAuthorizer"

  override val emailRepo: MemberEmailRepository = InMemoryMemberEmailRepository
  override val appStateRepo: ApplicationStateRepository =
    InMemoryApplicationStateRepository

  val tokenStore: StaticAccessTokenStore.type = StaticAccessTokenStore
  override def getAccessToken: AccessToken = tokenStore.getOrCreateAccessToken

  override def incrementRetryCount: Unit = {}
}
