package vchat.state.models.values

import vchat.utilities.time.AppTime

object TimeoutSessionIDStatus extends AppTime {
  def create: TimeoutSessionIDStatus =
    TimeoutSessionIDStatus(
      currentTimeMillis + defaultTokenTimeout
    )
}

case class TimeoutSessionIDStatus(timeoutUTC: Long)
    extends SessionIDStatus
    with AppTime {
  def exists = true
  def expired: Boolean = timeoutUTC < currentTimeMillis
}
