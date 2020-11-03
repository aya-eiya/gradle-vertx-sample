package vchat.auth.api.queries

import vchat.auth.domain.models.values.email._
import vchat.auth.domain.repositories.{
  ApplicationStateRepository,
  MemberEmailRepository
}
import vchat.utilities.email.EmailAddress

trait GetEmailAuth {
  val emailRepo: MemberEmailRepository
  val appStateRepo: ApplicationStateRepository

  def createStateAndReturnAuthNStatus: EmailAuthNStatus

  def verifyEmailAddress(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): Either[EmailAuthNErrorStatus, Unit] =
    Either.cond(
      EmailAddress.isValid(emailAddress.value),
      verifyPassword(emailAddress, rawPassword),
      EmailAuthNErrorStatus(EmailAuthNErrorStatus.wrongEmailAddressErrorCode)
    )
  def verifyPassword(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): Either[EmailAuthNErrorStatus, EmailAuthNStatus] =
    Either.cond(
      emailRepo.exists(emailAddress, rawPassword),
      createStateAndReturnAuthNStatus,
      EmailAuthNErrorStatus(EmailAuthNErrorStatus.memberNotFound)
    )
}
