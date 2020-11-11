package vchat.state.api

import vchat.state.api.commands.{
  CreateAccessToken,
  CreateApplicationContext,
  SetContext
}
import vchat.state.api.queries.{VerifyAccessToken, GetApplicationContext}

trait ApplicationContextManager
    extends CreateAccessToken
    with VerifyAccessToken
    with CreateApplicationContext
    with GetApplicationContext
    with SetContext
