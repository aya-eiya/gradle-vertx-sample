package vchat.auth

trait Role {}

/**
  * 認可状態を表す
  */
trait AuthZStatus {
  def hasRight(role: Role): Boolean
}

/**
  *
  */
trait AuthZ {
  def verifyToken(token: AuthToken): AuthZStatus
}
