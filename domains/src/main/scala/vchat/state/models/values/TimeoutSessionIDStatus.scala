package vchat.state.models.values

import vchat.time.TimeScale

object TimeoutSessionIDStatus {
  def create: TimeoutSessionIDStatus = TimeoutSessionIDStatus(TimeScale.default)
}

case class TimeoutSessionIDStatus(
    timer: TimeScale,
    timeoutUTC: Long = 30 * 1000
) extends SessionIDStatus {
  private val createdAt = timer.epochMilli
  def exists = true
  def expired: Boolean = timeoutUTC < timer.epochMilli - createdAt
}
