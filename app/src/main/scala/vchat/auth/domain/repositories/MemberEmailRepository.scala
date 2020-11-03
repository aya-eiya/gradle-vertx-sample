package vchat.auth.domain.repositories

import vchat.auth.domain.models.values.email.AuthEmailAddress

trait MemberEmailRepository {
  def exists(emailAddress: AuthEmailAddress, rawPassword: String): Boolean
}
