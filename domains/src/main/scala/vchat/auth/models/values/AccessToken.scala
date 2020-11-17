package vchat.auth.models.values

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64

import vchat.state.models.values.SessionID

object AccessToken {
  val salt = "hi#t @Aj%B i2+4 bdLk"
  val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
  private def getCheckSum(token: SessionID): Array[Byte] =
    digest.digest(token.toString.getBytes(StandardCharsets.UTF_8))

}

case class AccessToken(token: SessionID) {
  import AccessToken._
  def base64: String = Base64.getEncoder.encodeToString(getCheckSum(token))
}
