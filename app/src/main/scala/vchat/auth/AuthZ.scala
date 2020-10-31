package vchat.auth

trait Role {}

/**
  * 認可状態を表す
  */
trait AuthZStatus {
  val role: Role
  def hasRight: Boolean
}

/**
  *
  */
trait AuthZ {
  def verifyToken(token: AuthToken): AuthZStatus
}
