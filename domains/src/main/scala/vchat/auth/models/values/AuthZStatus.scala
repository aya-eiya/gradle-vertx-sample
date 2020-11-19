package vchat.auth.models.values

/**
  * 認可状態を表す
  */
trait AuthZStatus {
  val privilege: AuthZPrivilege
  def hasRight: Boolean
}
