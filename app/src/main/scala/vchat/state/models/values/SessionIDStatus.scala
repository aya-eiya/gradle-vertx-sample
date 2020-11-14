package vchat.state.models.values

object SessionIDStatus {
  private case class _SessionIDStatus(
      exists: Boolean,
      expired: Boolean
  ) extends SessionIDStatus
  def NotExists: SessionIDStatus =
    _SessionIDStatus(exists = false, expired = false)
}

trait SessionIDStatus {
  def exists: Boolean
  def expired: Boolean
  def existsAndNotExpired: Boolean = exists && !expired
}
