package vchat.server.error

import vchat.logging.models.ErrorStatus

trait ServiceErrorStatus extends ErrorStatus {
  override val code: ServiceErrorCode
}
