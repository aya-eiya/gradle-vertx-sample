package vchat.auth.domain.models

import vchat.state.models.values.AccessToken

trait Context

case class LoginContext(
    accessToken: AccessToken,
    authNStatus: AuthNStatus
) extends Context

case class ApplicationContext(
    loginContext: LoginContext
) extends Context
