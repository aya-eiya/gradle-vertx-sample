package vchat.auth.domain.models.values

case class AuthNErrorCode(code: Int, message: String) {
  def describe: String = s"[AuthNError:$code]$message"
}
