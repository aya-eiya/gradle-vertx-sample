package vchat.auth.domain.models

import vchat.state.models.Context
import vchat.state.models.values.AccessToken

case class LoginContext(
    accessToken: AccessToken,
    authNStatus: AuthNStatus
) extends Context
