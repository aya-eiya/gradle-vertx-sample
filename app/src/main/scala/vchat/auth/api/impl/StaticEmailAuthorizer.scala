package vchat.auth.api.impl

import vchat.auth.api.commands.CreateEmailAuth
import vchat.auth.api.queries.GetEmailAuth
import vchat.auth.domain.models.Authorizer
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

object StaticEmailAuthorizer
    extends Authorizer
    with GetEmailAuth
    with CreateEmailAuth {

  override val emailRepo: MemberEmailRepository = InMemoryMemberEmailRepository
  override val appStateRepo: ApplicationStateRepository =
    InMemoryApplicationStateRepository
  override val AuthorizerId: String = "StaticEmailAuthorizer"

  val tokenStore: StaticAccessTokenStore.type = StaticAccessTokenStore
  override def getAccessToken: AccessToken = tokenStore.getOrCreateAccessToken

  override def incrementRetryCount: Unit = {}
}
