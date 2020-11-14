package vchat.state.models

import vchat.state.models.values.SessionIDStatus

case class AccessContext(status: SessionIDStatus) extends Context
