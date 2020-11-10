package vchat.auth.api.impl

import vchat.app.service.base.UseWebApplicationContext
import vchat.auth.api.EmailAuthorizer
import vchat.auth.domain.models.{AuthN, LoginContext}
import vchat.auth.domain.models.values.{AuthNErrorStatus, AuthNStatus}
import vchat.auth.domain.models.values.email.AuthEmailAddress
import vchat.state.api.ApplicationContextManager
import vchat.state.api.impl.StaticApplicationContextManager

class EmailAuth(val emailAddress: AuthEmailAddress, val rawPassword: String)
    extends AuthN
    with UseWebApplicationContext {
  def authorizer: EmailAuthorizer = StaticEmailAuthorizer
  override def contextManager: ApplicationContextManager =
    StaticApplicationContextManager

  override def tryAuth(): Either[AuthNErrorStatus, AuthNStatus] = {
    val token = createToken
    for {
      s <- authorizer.verifyPassword(token, emailAddress, rawPassword)
      _ = contextManager.setContext(token, LoginContext(token, s))
    } yield s
  }

}
