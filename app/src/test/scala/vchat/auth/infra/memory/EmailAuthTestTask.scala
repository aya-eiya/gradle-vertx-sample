package vchat.auth.infra.memory

import cats.data.EitherT
import cats.effect.IO
import vchat.auth.api.email.EmailAuthorizer
import vchat.auth.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus,
  EmailAuthNStatus
}
import vchat.auth.models.LoginContext
import vchat.logging.models.ErrorDescription
import vchat.server.UseWebApplicationContext
import vchat.state.api.ApplicationContextManager

case class EmailAuthTestTask(
    emailAddress: AuthEmailAddress,
    rawPassword: String
) extends UseWebApplicationContext {
  def authorizer: EmailAuthorizer = InMemoryEmailAuthorizer

  private def failedToCreateToken: ErrorDescription =
    ErrorDescription(
      reason = "アクセストークンの生成に失敗しました",
      todo = "",
      reference = ""
    )
  override def contextManager: ApplicationContextManager =
    InMemoryApplicationContextManager

  def tryAuth(): EitherT[IO, EmailAuthNErrorStatus, EmailAuthNStatus] =
    for {
      token <- EitherT.right(createSessionId)
      s <- authorizer.verifyPassword(token, emailAddress, rawPassword)
      _ <-
        contextManager
          .setContext(token, LoginContext(token, s))
          .toRight(
            EmailAuthNErrorStatus(
              EmailAuthNErrorStatus.systemErrorCode,
              failedToCreateToken
            )
          )
    } yield s

}
