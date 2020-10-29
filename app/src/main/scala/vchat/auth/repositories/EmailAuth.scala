package vchat.auth.repositories

import vchat.auth._
import vchat.utilities.email.EmailAddress

case class EmailAuthNStatus(
    override val token: AuthToken,
    override val isAuthed: Boolean,
    override val retryTimes: Int
) extends AuthNStatus {
  override val maxRetryTime = 3
}
object EmailAuthNError {
  val wrongEmailAddressErrorCode = AuthNErrorCode(100001, "Wrong email address")
}
case class EmailAuthNError(override val code: AuthNErrorCode) extends AuthNError

class MailAuth(val emailAddress: String, val encryptedPassword: String)
    extends AuthN {
  override def tryAuth(): Either[EmailAuthNError, EmailAuthNStatus] = {
    Either.cond(
      EmailAddress.isValid(emailAddress),
      EmailAuthNStatus(
        AuthToken("hoge"),
        true,
        0
      ),
      EmailAuthNError(EmailAuthNError.wrongEmailAddressErrorCode)
    )
  }
}
