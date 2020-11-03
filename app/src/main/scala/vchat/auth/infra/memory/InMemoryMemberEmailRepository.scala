package vchat.auth.infra.memory

import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EncryptedPassword
}
import vchat.auth.domain.repositories._

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
  ): Boolean =
    data
      .get(emailAddress)
      .exists(_.check(rawPassword))
}
