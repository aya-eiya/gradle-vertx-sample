package vchat.auth.api.commands

import vchat.auth.domain.models.values.AuthToken
import vchat.auth.domain.models.values.email.EmailAuthNStatus
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

trait CreateEmailAuth {
  val appStateRepo: ApplicationContextRepository

  def getAccessToken: AccessToken

  def incrementRetryCount: Unit

  def createStateAndReturnAuthNStatus: EmailAuthNStatus = {
    val authToken = AuthToken(getAccessToken)
    val authNStatus = EmailAuthNStatus(authToken, isAuthed = true)
    authNStatus
  }
}
