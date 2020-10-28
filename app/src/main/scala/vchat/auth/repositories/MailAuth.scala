package vchat.auth.repositories

import vchat.auth.AuthN

class MailAuth extends AuthN {
  def isAuthed: Boolean = false
}
