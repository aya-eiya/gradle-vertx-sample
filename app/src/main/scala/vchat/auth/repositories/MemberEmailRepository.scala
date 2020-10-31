package vchat.auth.repositories

case class AuthEmailAddress(emailAddress: String);
case class EncryptedPassword(password: String)

trait MemberEmailRepository {}
