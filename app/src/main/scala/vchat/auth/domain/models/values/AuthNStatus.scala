package vchat.auth.domain.models.values

import vchat.state.models.values.SessionID

/**
  * 認証状態を示す
  */
trait AuthNStatus {
  val token: AccessToken
  val isAuthed: Boolean
  val retryTimes: Int
  val maxRetryTime: Int
}
object AuthNStatus {
  private case class EmptyAuthNStatus(override val token: AccessToken)
      extends AuthNStatus {
    override val isAuthed: Boolean = false
    override val retryTimes: Int = 0
    override val maxRetryTime: Int = Int.MaxValue
  }
  def empty(token: SessionID): AuthNStatus =
    EmptyAuthNStatus(AccessToken(token))
}
