package vchat.auth.api.commands

import vchat.auth.domain.models.values.AuthToken
import vchat.auth.domain.models.values.email.EmailAuthNStatus
import vchat.auth.domain.repositories.ApplicationStateRepository
import vchat.state.models.values.AccessToken

trait CreateEmailAuth {
  val appStateRepo: ApplicationStateRepository

  def createAccessToken: AccessToken

  def createStateAndReturnAuthNStatus: EmailAuthNStatus = {
    val accessToken = createAccessToken
    val authToken = AuthToken(accessToken)
    val authNStatus = EmailAuthNStatus(authToken, isAuthed = true)
    appStateRepo.create(accessToken, authNStatus)
    authNStatus
  }
}
