package vchat.auth.api.email

import vchat.auth.api.Authorizer
import vchat.auth.api.email.commands.CreateEmailAuth
import vchat.auth.api.email.queries.GetEmailAuth

trait EmailAuthorizer extends Authorizer with GetEmailAuth with CreateEmailAuth
