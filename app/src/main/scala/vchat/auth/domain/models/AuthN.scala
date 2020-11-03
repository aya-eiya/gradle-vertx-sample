package vchat.auth.domain.models

import vchat.auth.domain.models.values.{AuthNErrorCode, AuthToken}

/**
  * 認証状態を示す
  */
trait AuthNStatus {
  val token: AuthToken
  val isAuthed: Boolean
  val retryTimes: Int
  val maxRetryTime: Int
}

trait AuthNErrorStatus {
  val code: AuthNErrorCode
}

trait AuthN {
  type AuthNResult = Either[AuthNErrorStatus, AuthNStatus]
  def tryAuth(): AuthNResult
}
