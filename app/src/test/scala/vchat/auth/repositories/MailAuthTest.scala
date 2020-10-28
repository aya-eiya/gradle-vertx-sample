package vchat.auth.repositories

import org.scalatest.funspec.AnyFunSpec

class MailAuthTest extends AnyFunSpec {
  def empty = Set.empty
  describe("scala test check") {
    it("assertion check") {
      info(this.getClass.getName)
      assert(empty.isEmpty,"fail check")
    }
  }
}
