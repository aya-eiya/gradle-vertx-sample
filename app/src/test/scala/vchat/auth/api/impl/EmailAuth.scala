package vchat.auth.api.impl

import cats.data.EitherT
import cats.effect.IO
import vchat.app.service.base.UseWebApplicationContext
import vchat.auth.api.EmailAuthorizer
import vchat.auth.domain.models.{AuthN, LoginContext}
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus,
  EmailAuthNStatus
}
import vchat.logging.ErrorDescription
import vchat.state.api.ApplicationContextManager
import vchat.state.api.impl.StaticApplicationContextManager

class EmailAuth(val emailAddress: AuthEmailAddress, val rawPassword: String)
    extends AuthN
    with UseWebApplicationContext {
  def authorizer: EmailAuthorizer = StaticEmailAuthorizer

  def failedToCreateToken: ErrorDescription =
    ErrorDescription(
      reason = "アクセストークンの生成に失敗しました",
      todo = "",
      reference = ""
    )
  override def contextManager: ApplicationContextManager =
    StaticApplicationContextManager

  override def tryAuth()
      : EitherT[IO, EmailAuthNErrorStatus, EmailAuthNStatus] = {
    for {
      token <- EitherT.right(createToken)
      s <- authorizer.verifyPassword(token, emailAddress, rawPassword)
      _ = contextManager.setContext(token, LoginContext(token, s))
    } yield s
  }

}
