package vchat.auth.models

import cats.effect.IO
import vchat.auth.models.values.AccessToken

trait Privilege {}

/**
  * 認可状態を表す
  */
trait AuthZStatus {
  val privilege: Privilege
  def hasRight: Boolean
}

/**
  *
  */
trait AuthZ {
  def verifyToken(token: AccessToken): IO[AuthZStatus]
}
