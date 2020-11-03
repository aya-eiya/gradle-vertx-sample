package vchat.auth.infra

import vchat.auth._
import vchat.auth.repositories.AuthEmailAddress
import vchat.auth.infra.memory.StaticEmailAuthorizer

case class EmailAuthNStatus(
    override val token: AuthToken,
    override val isAuthed: Boolean = false,
    override val retryTimes: Int = 0
) extends AuthNStatus {
  override val maxRetryTime = 3
}
case class EmailAuthNError(override val code: AuthNErrorCode) extends AuthNError

object EmailAuthNError {
  val wrongEmailAddressErrorCode: AuthNErrorCode =
    AuthNErrorCode(code = 100001, message = "Wrong email address")
  val memberNotFound: AuthNErrorCode =
    AuthNErrorCode(code = 100002, message = "Wrong email address")
}

class EmailAuth(val emailAddress: AuthEmailAddress, val rawPassword: String)
    extends AuthN {
  private val authorizer = StaticEmailAuthorizer
  override def tryAuth(): Either[EmailAuthNError, EmailAuthNStatus] =
    authorizer.verifyPassword(emailAddress, rawPassword)
}
