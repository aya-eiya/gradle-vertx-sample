package vchat.auth.api.impl

import vchat.auth.domain.models._
import vchat.auth.domain.models.values.email._

class EmailAuth(val emailAddress: AuthEmailAddress, val rawPassword: String)
    extends AuthN {
  private val authorizer = StaticEmailAuthorizer
  override def tryAuth(): Either[AuthNErrorStatus, AuthNStatus] =
    authorizer.verifyPassword(emailAddress, rawPassword)
}
