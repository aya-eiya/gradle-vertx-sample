package vchat.auth

case class AuthToken(token: String)
case class AuthNErrorCode(code: Int, message: String) {
  def describe: String = s"[AuthNError:$code]$message"
}

/**
  * 認証状態を示す
  */
trait AuthNStatus {
  val token: AuthToken
  val isAuthed: Boolean
  val retryTimes: Int
  val maxRetryTime: Int
}

trait AuthNError {
  val code: AuthNErrorCode
}

trait AuthN {
  type AuthNResult = Either[AuthNError, AuthNStatus]
  def tryAuth(): AuthNResult
}
