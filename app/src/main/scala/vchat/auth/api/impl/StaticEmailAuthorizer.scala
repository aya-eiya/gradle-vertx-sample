package vchat.auth.api.impl

import vchat.auth.domain.models.EmailAuthorizer
import vchat.auth.domain.repositories.MemberEmailRepository
import vchat.auth.infra.memory.{
  InMemoryApplicationContextRepository,
  InMemoryMemberEmailRepository
}
import vchat.state.api.impl.StaticAccessTokenStore
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

object StaticEmailAuthorizer extends EmailAuthorizer {

  override val AuthorizerId: String = "StaticEmailAuthorizer"

  override val emailRepo: MemberEmailRepository = InMemoryMemberEmailRepository
  override val appStateRepo: ApplicationContextRepository =
    InMemoryApplicationContextRepository

  val tokenStore: StaticAccessTokenStore.type = StaticAccessTokenStore
  override def getAccessToken: AccessToken = tokenStore.getOrCreateAccessToken

  override def incrementRetryCount: Unit = {}
}
