package vchat.auth.domain.models

import cats.data.EitherT
import cats.effect.IO
import vchat.auth.domain.models.values.{AuthNErrorStatus, AuthNStatus}

trait AuthN {
  type AuthNResult = EitherT[IO, _ <: AuthNErrorStatus, _ <: AuthNStatus]
  def tryAuth(): AuthNResult
}
