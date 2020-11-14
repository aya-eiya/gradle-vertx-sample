package vchat.auth.domain.models

import cats.effect.IO
import vchat.auth.domain.models.values.AccessToken

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
  def verifyToken(token: AccessToken): IO[AuthZStatus]
}
