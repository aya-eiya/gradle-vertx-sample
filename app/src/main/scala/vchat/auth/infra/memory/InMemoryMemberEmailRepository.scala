package vchat.auth.infra.memory

import cats.data.OptionT
import cats.effect.IO
import vchat.auth.models.values.email.{AuthEmailAddress, EncryptedPassword}
import vchat.auth.repositories._

object InMemoryMemberEmailRepository extends MemberEmailRepository {
  private def rawEncryptor(s: String) = s
  private val data: Map[AuthEmailAddress, EncryptedPassword] = Map(
    AuthEmailAddress("test@test.jp") -> EncryptedPassword(
      "rightPassword",
      rawEncryptor
    )
  )

  override def exists(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): OptionT[IO, Boolean] =
    OptionT(
      IO(
        if (
          data
            .get(emailAddress)
            .exists(_.check(rawPassword))
        ) Some(true)
        else None
      )
    )
}
