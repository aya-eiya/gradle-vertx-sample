package vchat.state.models

import vchat.state.models.values.AccessTokenStatus

case class AccessContext(status: AccessTokenStatus) extends Context
