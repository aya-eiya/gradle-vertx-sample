package vchat.auth.api.commands

import cats.effect.IO
import vchat.auth.domain.models.values.AuthToken
import vchat.auth.domain.models.values.email.EmailAuthNStatus
import vchat.state.models.values.AccessToken

trait CreateEmailAuth {
  def incrementRetryCount(): Unit
  def createAuthNStatus(
      accessToken: AccessToken
  ): IO[EmailAuthNStatus] =
    IO(EmailAuthNStatus(AuthToken(accessToken), isAuthed = true))

}
