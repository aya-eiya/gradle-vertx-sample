package vchat.auth.domain.repositories

import cats.effect.IO
import vchat.auth.domain.models.values.email.AuthEmailAddress

trait MemberEmailRepository {
  def exists(emailAddress: AuthEmailAddress, rawPassword: String): IO[Boolean]
}
