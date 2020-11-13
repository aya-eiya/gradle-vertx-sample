package vchat.logging

trait ErrorStatus { self =>
  type A >: self.type
  val code: ErrorCode
  val description: ErrorDescription
  def toLeft[T <: A]: Left[T, Nothing] = Left(this.asInstanceOf[T])
}
