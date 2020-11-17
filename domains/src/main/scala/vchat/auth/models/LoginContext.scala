package vchat.auth.models

import vchat.auth.models.values.AuthNStatus
import vchat.state.models.Context
import vchat.state.models.values.SessionID

case class LoginContext(
    sessionID: SessionID,
    authNStatus: AuthNStatus
) extends Context
