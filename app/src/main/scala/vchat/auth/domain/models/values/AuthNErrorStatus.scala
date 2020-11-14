package vchat.auth.domain.models.values

import vchat.logging.models.ErrorStatus

trait AuthNErrorStatus extends ErrorStatus {
  val code: AuthNErrorCode
}
