package vchat.auth.domain.models

import vchat.auth.api.commands.CreateEmailAuth
import vchat.auth.api.queries.GetEmailAuth

trait Authorizer {
  val AuthorizerId: String
}

trait EmailAuthorizer extends Authorizer with GetEmailAuth with CreateEmailAuth
