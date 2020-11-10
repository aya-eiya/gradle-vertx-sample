package vchat.auth.api.commands

import vchat.auth.domain.models.values.AuthToken
import vchat.auth.domain.models.values.email.EmailAuthNStatus
import vchat.state.models.values.AccessToken

trait CreateEmailAuth {
  def incrementRetryCount(): Unit
  def createStateAndReturnAuthNStatus(
      accessToken: AccessToken
  ): EmailAuthNStatus = {
    val authToken = AuthToken(accessToken)
    val authNStatus = EmailAuthNStatus(authToken, isAuthed = true)
    authNStatus
  }
}
