package vchat.logging.repository

import vchat.logging.{ErrorCode, ErrorDescription, ErrorStatus}

case class RepositoryErrorStatus(code: ErrorCode, description: ErrorDescription)
    extends ErrorStatus
