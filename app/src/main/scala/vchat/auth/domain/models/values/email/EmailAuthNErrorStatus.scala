package vchat.auth.domain.models.values.email

import vchat.auth.domain.models.values._
import vchat.logging.ErrorDescription

case class EmailAuthNErrorStatus(
    override val code: AuthNErrorCode,
    override val description: ErrorDescription
) extends AuthNErrorStatus

object EmailAuthNErrorStatus {

  val wrongEmailAddressErrorCode: AuthNErrorCode =
    AuthNErrorCode(code = 100001, message = "Wrong email address")
  val memberNotFound: AuthNErrorCode =
    AuthNErrorCode(code = 100002, message = "Member not found")
  val invalidAccessTokenErrorCode: AuthNErrorCode =
    AuthNErrorCode(code = 100003, message = "Invalid access token")
  val systemErrorCode: AuthNErrorCode =
    AuthNErrorCode(code = 100000, message = "System error in email auth")

}
