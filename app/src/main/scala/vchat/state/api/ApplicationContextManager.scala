package vchat.state.api

import vchat.state.api.commands.{
  CreateAccessToken,
  CreateApplicationContext,
  SetContext
}
import vchat.state.api.queries.{GetAccessToken, GetApplicationContext}

trait ApplicationContextManager
    extends CreateAccessToken
    with GetAccessToken
    with CreateApplicationContext
    with GetApplicationContext
    with SetContext
