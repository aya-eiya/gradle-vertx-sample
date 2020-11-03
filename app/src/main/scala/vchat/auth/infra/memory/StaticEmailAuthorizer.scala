package vchat.auth.infra.memory

import vchat.auth.{AuthToken, Authorizer}
import vchat.auth.infra.{EmailAuthNError, EmailAuthNStatus}
import vchat.auth.repositories.{
  ApplicationStateRepository,
  AuthEmailAddress,
  MemberEmailRepository
}
import vchat.state.infra.memory.InMemoryAccessTokenRepository
import vchat.state.repositories.AccessTokenRepository
import vchat.utilities.email.EmailAddress

object StaticEmailAuthorizer extends Authorizer {

  val emailRepo: MemberEmailRepository = InMemoryMemberEmailRepository
  val tokenRepo: AccessTokenRepository = InMemoryAccessTokenRepository
  val appStateRepo: ApplicationStateRepository =
    InMemoryApplicationStateRepository

  def verifyEmailAddress(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): Either[EmailAuthNError, Unit] =
    Either.cond(
      EmailAddress.isValid(emailAddress.value),
      verifyPassword(emailAddress, rawPassword),
      EmailAuthNError(EmailAuthNError.wrongEmailAddressErrorCode)
    )
  def verifyPassword(
      emailAddress: AuthEmailAddress,
      rawPassword: String
  ): Either[EmailAuthNError, EmailAuthNStatus] =
    Either.cond(
      emailRepo.exists(emailAddress, rawPassword),
      createStateAndReturnAuthStatus,
      EmailAuthNError(EmailAuthNError.memberNotFound)
    )
  def createStateAndReturnAuthStatus: EmailAuthNStatus = {
    val accessToken = tokenRepo.create()
    val authToken = AuthToken(accessToken)
    val authNStatus = EmailAuthNStatus(authToken, isAuthed = true)
    appStateRepo.create(accessToken, authNStatus)
    authNStatus
  }

  override val AuthorizerId: String = "StaticEmailAuthorizer"

}
