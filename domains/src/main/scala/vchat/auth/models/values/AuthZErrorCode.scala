package vchat.auth.models.values

import vchat.logging.models.ErrorCode

case class AuthZErrorCode(code: Int, message: String) extends ErrorCode {
  val errorType = "AuthZError"
}
