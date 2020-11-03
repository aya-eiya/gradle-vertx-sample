package vchat.auth

import vchat.state.AccessToken

case class AuthToken(token: AccessToken)
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

trait Authorizer {
  val AuthorizerId: String
}

trait AuthN {
  type AuthNResult = Either[AuthNError, AuthNStatus]
  def tryAuth(): AuthNResult
}
