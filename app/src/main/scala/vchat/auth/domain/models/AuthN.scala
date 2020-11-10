package vchat.auth.domain.models

import vchat.auth.domain.models.values.{AuthNErrorStatus, AuthNStatus}

trait AuthN {
  type AuthNResult = Either[AuthNErrorStatus, AuthNStatus]
  def tryAuth(): AuthNResult
}
