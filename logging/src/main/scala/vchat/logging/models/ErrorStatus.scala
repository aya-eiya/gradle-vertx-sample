package vchat.logging.models

trait ErrorStatus {
  val code: ErrorCode
  val description: ErrorDescription
  def toLeft[T]: Left[T, Nothing] = Left(this.asInstanceOf[T])
}
