package vchat.state.api

import vchat.state.api.commands.{
  CreateSessionID,
  CreateApplicationContext,
  SetContext
}
import vchat.state.api.queries.{VerifySessionID, GetApplicationContext}

trait ApplicationContextManager
    extends CreateSessionID
    with VerifySessionID
    with CreateApplicationContext
    with GetApplicationContext
    with SetContext
