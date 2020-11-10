package vchat.state.models

case class ApplicationContext(childContexts: Seq[Context]) extends Context
