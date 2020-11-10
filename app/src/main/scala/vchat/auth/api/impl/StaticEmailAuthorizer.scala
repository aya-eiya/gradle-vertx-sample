package vchat.auth.api.impl

import vchat.auth.api.EmailAuthorizer
import vchat.auth.domain.repositories.MemberEmailRepository
import vchat.auth.infra.memory.InMemoryMemberEmailRepository

object StaticEmailAuthorizer extends EmailAuthorizer {

  override val AuthorizerId: String = "StaticEmailAuthorizer"

  override val emailRepo: MemberEmailRepository = InMemoryMemberEmailRepository

  override def incrementRetryCount(): Unit = {}

}
