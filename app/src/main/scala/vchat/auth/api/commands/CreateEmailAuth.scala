package vchat.auth.api.commands

import cats.effect.IO
import vchat.auth.domain.models.values.AuthToken
import vchat.auth.domain.models.values.email.EmailAuthNStatus
import vchat.state.models.values.SessionID

trait CreateEmailAuth {
  def incrementRetryCount(): Unit
  def createAuthNStatus(
      sessionID: SessionID
  ): IO[EmailAuthNStatus] =
    IO(EmailAuthNStatus(AuthToken(sessionID), isAuthed = true))

}
