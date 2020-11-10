package vchat.utilities.email

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class EmailAddressTest extends AnyFunSpec with Matchers {
  describe("EmailAddressの検証") {
    it("正しい形式のメールアドレス") {
      assert(EmailAddress.isValid("test@test.jp"))
    }
    it("間違った形式のメールアドレス") {
      assert(!EmailAddress.isValid("test_test.jp"))
    }
  }
}
