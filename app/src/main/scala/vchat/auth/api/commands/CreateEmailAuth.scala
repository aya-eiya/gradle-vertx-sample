package vchat.auth.api.commands

import vchat.auth.domain.models.values.AuthToken
import vchat.auth.domain.models.values.email.EmailAuthNStatus
import vchat.state.models.values.AccessToken

trait CreateEmailAuth {
  def incrementRetryCount(): Unit
  def createAuthNStatus(
      accessToken: AccessToken
  ): EmailAuthNStatus =
    EmailAuthNStatus(AuthToken(accessToken), isAuthed = true)

}
