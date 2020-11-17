package vchat.auth.models.values.email

import vchat.auth.models.values.{AccessToken, AuthNStatus}

case class EmailAuthNStatus(
    override val token: AccessToken,
    override val isAuthed: Boolean = false,
    override val retryTimes: Int = 0
) extends AuthNStatus {
  override val maxRetryTime = 3
}
