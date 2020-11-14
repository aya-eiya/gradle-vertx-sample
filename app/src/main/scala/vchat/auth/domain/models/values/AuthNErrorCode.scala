package vchat.auth.domain.models.values

import vchat.logging.models.ErrorCode

case class AuthNErrorCode(code: Int, message: String) extends ErrorCode {
  val errorType = "AuthNError"
}
