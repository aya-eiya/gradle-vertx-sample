package vchat.state.models.values

import vchat.utilities.time.AppTime

object TimeoutAccessTokenStatus extends AppTime {
  def create: TimeoutAccessTokenStatus =
    TimeoutAccessTokenStatus(
      currentTimeMillis + defaultTokenTimeout
    )
}

case class TimeoutAccessTokenStatus(timeoutUTC: Long)
    extends AccessTokenStatus
    with AppTime {
  def exists = true
  def expired: Boolean = timeoutUTC > currentTimeMillis
}
