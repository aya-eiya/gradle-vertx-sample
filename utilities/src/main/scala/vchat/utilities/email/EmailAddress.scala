package vchat.utilities.email

object EmailAddress {
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
