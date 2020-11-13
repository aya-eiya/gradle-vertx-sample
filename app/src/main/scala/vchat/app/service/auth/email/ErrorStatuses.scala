package vchat.app.service.auth.email

import vchat.auth.domain.models.values.email.EmailAuthNErrorStatus

trait ErrorStatuses extends ErrorDescriptions {
  def emailAddressNotFoundStatus: EmailAuthNErrorStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.memberNotFound,
      emailAddressNotFound
    )
  def passwordNotFoundStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.memberNotFound,
      passwordNotFound
    )
  def emailAddressAndPasswordNotFoundStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.memberNotFound,
      emailAddressAndPasswordNotFound
    )
  def invalidAccessTokenStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.invalidAccessTokenErrorCode,
      emailAddressAndPasswordNotFound
    )
  def failedToSetContextStatus =
    EmailAuthNErrorStatus(
      EmailAuthNErrorStatus.systemErrorCode,
      failedToSetContext
    )

}
