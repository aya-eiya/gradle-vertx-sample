package vchat.auth.infra.memory

import cats.effect.IO
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EncryptedPassword
}
import vchat.auth.domain.repositories._

trait InMemoryMemberEmailRepositoryImpl extends MemberEmailRepository {
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
  ): IO[Boolean] =
    IO(
      data
        .get(emailAddress)
        .exists(_.check(rawPassword))
    )
}

object InMemoryMemberEmailRepository extends InMemoryMemberEmailRepositoryImpl
