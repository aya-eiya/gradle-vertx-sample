package vchat.auth.domain.models.values

import vchat.state.models.values.AccessToken

/**
  * 認証状態を示す
  */
trait AuthNStatus {
  val token: AuthToken
  val isAuthed: Boolean
  val retryTimes: Int
  val maxRetryTime: Int
}
object AuthNStatus {
  private case class EmptyAuthNStatus(override val token: AuthToken)
      extends AuthNStatus {
    override val isAuthed: Boolean = false
    override val retryTimes: Int = 0
    override val maxRetryTime: Int = Int.MaxValue
  }
  def empty(token: AccessToken): AuthNStatus =
    EmptyAuthNStatus(AuthToken(token))
}
