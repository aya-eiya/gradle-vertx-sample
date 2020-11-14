package vchat.auth.domain.models

import vchat.auth.domain.models.values.AuthNStatus
import vchat.state.models.Context
import vchat.state.models.values.SessionID

case class LoginContext(
    sessionID: SessionID,
    authNStatus: AuthNStatus
) extends Context
