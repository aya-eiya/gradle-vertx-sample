package vchat.auth.domain.models.values.email

case class EncryptedPassword(value: String, encryptor: String => String) {
  def check(password: String): Boolean = encryptor(password) == value
}
