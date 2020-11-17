package vchat.auth.api

import vchat.auth.api.email.commands.CreateEmailAuth
import vchat.auth.api.email.queries.GetEmailAuth

trait Authorizer {
  val AuthorizerId: String
}
