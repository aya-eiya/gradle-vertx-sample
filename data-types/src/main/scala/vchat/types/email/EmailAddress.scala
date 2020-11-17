package vchat.types.email

object EmailAddress {
  type EmailAddressString = String

  final private val rDomain =
    """^([a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)$""".r
  final private val rEmail =
    """^([a-zA-Z0-9.!#$%&’'*+/=?^_`{|}~-]+)@([a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)$""".r

  def isValid(email: String) =
    email match {
      case rEmail(_, _) => true
      case _            => false
    }
}

case class EmailAddress(address: EmailAddress.EmailAddressString) {
  def isValid = EmailAddress.isValid(address)
}
