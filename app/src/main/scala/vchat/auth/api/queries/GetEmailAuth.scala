package vchat.auth.api.queries

import cats.data.EitherT
import cats.effect.IO
import vchat.auth.domain.models.values.email._
import vchat.auth.domain.repositories.MemberEmailRepository
import vchat.logging.ErrorDescription
import vchat.state.models.values.AccessToken
import vchat.utilities.email.EmailAddress

trait GetEmailAuth {
  val emailRepo: MemberEmailRepository

  def createAuthNStatus(
      accessToken: AccessToken
  ): IO[EmailAuthNStatus]

  def verifyPassword(
      accessToken: AccessToken,
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): EitherT[IO, EmailAuthNErrorStatus, EmailAuthNStatus] =
    for {
      _ <- verifyEmailAddress(emailAddress)
      _ <- EitherT(
        for {
          r <- emailRepo.exists(emailAddress, rawPassword)
        } yield Either.cond(
          r,
          (),
          EmailAuthNErrorStatus(
            EmailAuthNErrorStatus.memberNotFound,
            ErrorDescription(
              reason = "EmailAddressまたはPasswordが間違っています",
              todo = "EmailAddressとPasswordの組み合わせをご確認ください",
              reference = "[Link(Page:forget/email),Link(Page:forget/password)]"
            )
          )
        )
      )
      c <- EitherT.right(createAuthNStatus(accessToken))
    } yield c

  private def verifyEmailAddress(
      emailAddress: AuthEmailAddress
  ): EitherT[IO, EmailAuthNErrorStatus, Unit] =
    EitherT.cond(
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
}
