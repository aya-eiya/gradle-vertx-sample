package vchat.auth.domain.models.values

import vchat.logging.ErrorStatus

trait AuthNErrorStatus extends ErrorStatus {
  val code: AuthNErrorCode
}
