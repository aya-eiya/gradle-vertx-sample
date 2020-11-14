package vchat.logging.models

trait ErrorCode {
  val errorType: String
  val code: Int
  val message: String
  def describe: String = s"[$errorType:$code]$message"
}
