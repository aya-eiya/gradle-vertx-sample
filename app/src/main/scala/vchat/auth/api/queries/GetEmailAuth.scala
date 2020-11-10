package vchat.auth.api.queries

import vchat.auth.domain.models.values.email._
import vchat.auth.domain.repositories.MemberEmailRepository
import vchat.logging.ErrorDescription
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository
import vchat.utilities.email.EmailAddress

trait GetEmailAuth {
  val emailRepo: MemberEmailRepository
  val appStateRepo: ApplicationContextRepository

  protected def getAccessToken: AccessToken

  protected def createStateAndReturnAuthNStatus: EmailAuthNStatus

  private def verifyEmailAddress(
      emailAddress: AuthEmailAddress
  ): Either[EmailAuthNErrorStatus, Unit] =
    Either.cond(
      EmailAddress.isValid(emailAddress.value),
      Unit,
      EmailAuthNErrorStatus(
        EmailAuthNErrorStatus.wrongEmailAddressErrorCode,
        ErrorDescription(
          reason = "EmailAddressの形式が間違っています",
          todo = "EmailAddressの形式を確かめて再度送信してください",
          reference = "[Link(Page:help,Target:#login)]"
        )
      )
    )
  def verifyPassword(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): Either[EmailAuthNErrorStatus, EmailAuthNStatus] =
    for {
      _ <- verifyEmailAddress(emailAddress)
      r <- Either.cond(
        emailRepo.exists(emailAddress, rawPassword),
        createStateAndReturnAuthNStatus,
        EmailAuthNErrorStatus(
          EmailAuthNErrorStatus.memberNotFound,
          ErrorDescription(
            reason = "EmailAddressまたはPasswordが間違っています",
            todo = "EmailAddressとPasswordの組み合わせをご確認ください",
            reference = "[Link(Page:forget/email),Link(Page:forget/password)]"
          )
        )
      )
    } yield r
}
