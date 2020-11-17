package vchat.auth.api.email.queries

import cats.data.EitherT
import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import vchat.auth.models.values.email._
import vchat.auth.repositories.MemberEmailRepository
import vchat.logging.models.ErrorDescription
import vchat.state.models.values.SessionID
import vchat.types.email.EmailAddress

object GetEmailAuth {
  object ErrorDescriptions {
    def verifyErrorDescription: ErrorDescription =
      ErrorDescription(
        reason = "EmailAddressまたはPasswordが間違っています",
        todo = "EmailAddressとPasswordの組み合わせをご確認ください",
        reference = "[Link(Page:forget/email),Link(Page:forget/password)]"
      )

    def wrongEmailAddressDescription: ErrorDescription =
      ErrorDescription(
        reason = "EmailAddressの形式が間違っています",
        todo = "EmailAddressの形式を確かめて再度送信してください",
        reference = "[Link(Page:help,Target:#login)]"
      )
  }

  def verifyErrorStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.memberNotFound,
      ErrorDescriptions.verifyErrorDescription
    )

  def wrongEmailAddressErrorStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.wrongEmailAddressErrorCode,
      ErrorDescriptions.wrongEmailAddressDescription
    )
}
trait GetEmailAuth extends LazyLogging {
  import logger._
  import GetEmailAuth._
  val emailRepo: MemberEmailRepository

  def createAuthNStatus(
      sessionID: SessionID
  ): IO[EmailAuthNStatus]

  def verifyPassword(
      sessionID: SessionID,
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): EitherT[IO, EmailAuthNErrorStatus, EmailAuthNStatus] =
    for {
      _ <- verifyEmailAddress(emailAddress)
      a = emailRepo.exists(emailAddress, rawPassword)
      _ <- a.toRight(verifyErrorStatus)
      c <- EitherT.right(createAuthNStatus(sessionID))
    } yield c

  private def verifyEmailAddress(
      emailAddress: AuthEmailAddress
  ): EitherT[IO, EmailAuthNErrorStatus, Unit] =
    EitherT.cond(
      EmailAddress.isValid(emailAddress.value),
      Unit,
      wrongEmailAddressErrorStatus
    )
}
