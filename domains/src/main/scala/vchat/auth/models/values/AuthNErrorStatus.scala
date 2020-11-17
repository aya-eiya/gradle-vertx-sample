package vchat.auth.models.values

import vchat.logging.models.ErrorStatus

trait AuthNErrorStatus extends ErrorStatus {
  val code: AuthNErrorCode
}
