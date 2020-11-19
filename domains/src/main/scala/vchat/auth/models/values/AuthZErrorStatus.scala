package vchat.auth.models.values

import vchat.logging.models.ErrorStatus

trait AuthZErrorStatus extends ErrorStatus {
  val code: AuthZErrorCode
}
