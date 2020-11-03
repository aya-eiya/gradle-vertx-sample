package vchat.auth.repositories

case class AuthEmailAddress(value: String)
case class EncryptedPassword(value: String, encryptor: String => String) {
  def check(password: String): Boolean = encryptor(password) == value
}

trait MemberEmailRepository {
  def exists(emailAddress: AuthEmailAddress, rawPassword: String): Boolean
}
