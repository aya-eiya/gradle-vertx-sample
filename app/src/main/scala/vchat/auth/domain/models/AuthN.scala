package vchat.auth.domain.models

import vchat.auth.domain.models.values.{AuthNErrorCode, AuthToken}
import vchat.logging.ErrorStatus
import vchat.state.models.values.AccessToken

case class EmptyAuthNStatus(override val token: AuthToken) extends AuthNStatus {
  override val isAuthed: Boolean = false
  override val retryTimes: Int = 0
  override val maxRetryTime: Int = Int.MaxValue
}
object AuthNStatus {
  def empty(token: AccessToken) = EmptyAuthNStatus(AuthToken(token))
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

trait AuthNErrorStatus extends ErrorStatus {
  val code: AuthNErrorCode
}

trait AuthN {
  type AuthNResult = Either[AuthNErrorStatus, AuthNStatus]
  def tryAuth(): AuthNResult
}
