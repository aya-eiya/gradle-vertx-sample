package vchat.state.models.values

case class AccessToken(value: String)
object AccessTokenStatus {
  val NotExists: AccessTokenStatus =
    AccessTokenStatus(exists = false, expired = false)
}
case class AccessTokenStatus(exists: Boolean, expired: Boolean) {
  def existsAndNotExpired: Boolean = exists && !expired
}
