package vchat.app.service.auth.email.error

import vchat.auth.domain.models.values.email.EmailAuthNErrorStatus

trait ErrorStatuses extends ErrorDescriptions {
  def emailAddressNotFoundStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.memberNotFound,
      emailAddressNotFound
    )
  def passwordNotFoundStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.memberNotFound,
      passwordNotFound
    )
  def emailAddressAndPasswordNotFoundStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.memberNotFound,
      emailAddressAndPasswordNotFound
    )
  def invalidSessionIDStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.invalidSessionIDErrorCode,
      emailAddressAndPasswordNotFound
    )
  def failedToSetContextStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.systemErrorCode,
      failedToSetContext
    )

}
