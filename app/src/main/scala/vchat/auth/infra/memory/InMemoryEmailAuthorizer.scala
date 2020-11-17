package vchat.auth.infra.memory

import vchat.auth.api.email.EmailAuthorizer
import vchat.auth.repositories.MemberEmailRepository

object InMemoryEmailAuthorizer extends EmailAuthorizer {

  override val AuthorizerId: String = "StaticEmailAuthorizer"

  override val emailRepo: MemberEmailRepository = InMemoryMemberEmailRepository

  override def incrementRetryCount(): Unit = {}

}
