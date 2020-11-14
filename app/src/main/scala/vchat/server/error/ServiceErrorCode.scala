package vchat.server.error

import vchat.logging.models.ErrorCode

case class ServiceErrorCode(code: Int, message: String) extends ErrorCode {
  override val errorType: String = "WebApplicationError"
}
