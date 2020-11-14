package vchat.logging.models.repository

import vchat.logging.models.{ErrorCode, ErrorDescription, ErrorStatus}

case class RepositoryErrorStatus(code: ErrorCode, description: ErrorDescription)
    extends ErrorStatus
