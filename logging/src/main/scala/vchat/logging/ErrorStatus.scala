package vchat.logging

trait ErrorStatus {
  val code: ErrorCode
  val description: ErrorDescription
}

case class ErrorDescription(
    reason: String,
    todo: String,
    reference: String
)
