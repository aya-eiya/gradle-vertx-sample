package vchat.state

case class AccessToken(value: String)
object AccessTokenStatus {
  val NotExists = AccessTokenStatus(exists = false, expired = false)
}
case class AccessTokenStatus(exists: Boolean, expired: Boolean)
