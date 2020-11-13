package vchat.auth.domain.repositories

import cats.data.OptionT
import cats.effect.IO
import vchat.auth.domain.models.values.email.AuthEmailAddress

trait MemberEmailRepository {
  def exists(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): OptionT[IO, Boolean]
}
