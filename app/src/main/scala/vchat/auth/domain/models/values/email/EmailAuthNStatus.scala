package vchat.auth.domain.models.values.email

import vchat.auth.domain.models.values.{AuthToken, AuthNStatus}

case class EmailAuthNStatus(
    override val token: AuthToken,
    override val isAuthed: Boolean = false,
    override val retryTimes: Int = 0
) extends AuthNStatus {
  override val maxRetryTime = 3
}
