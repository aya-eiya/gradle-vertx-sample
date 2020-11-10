package vchat.state.models.values

object AccessTokenStatus {
  private case class _AccessTokenStatus(
      exists: Boolean,
      expired: Boolean
  ) extends AccessTokenStatus
  def NotExists: AccessTokenStatus =
    _AccessTokenStatus(exists = false, expired = false)
}

trait AccessTokenStatus {
  def exists: Boolean
  def expired: Boolean
  def existsAndNotExpired: Boolean = exists && !expired
}
