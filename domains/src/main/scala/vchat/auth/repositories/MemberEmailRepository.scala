package vchat.auth.repositories

import cats.data.OptionT
import cats.effect.IO
import vchat.auth.models.values.email.AuthEmailAddress

trait MemberEmailRepository {
  def exists(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): OptionT[IO, Boolean]
}
