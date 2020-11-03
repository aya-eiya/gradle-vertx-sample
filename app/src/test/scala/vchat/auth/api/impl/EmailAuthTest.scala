package vchat.auth.api.impl

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import vchat.auth.domain.models.values.email.AuthEmailAddress
import vchat.auth.domain.repositories.ApplicationStateRepository
import vchat.auth.infra.memory.InMemoryApplicationStateRepository

class EmailAuthTest extends AnyFunSpec with Matchers {
  val appContext: ApplicationStateRepository =
    InMemoryApplicationStateRepository

  describe("メールアドレスによる認証ができる") {
    describe("メールアドレスが形式として正しい場合") {
      describe("パスワードが正しい場合") {
        val result =
          new EmailAuth(AuthEmailAddress("test@test.jp"), "rightPassword")
            .tryAuth()
        it("AuthNが認証済みになる") {
          assert(result.isRight)
          assert(result.exists(_.isAuthed))
        }
        it("認証済みのAuthNがログインコンテキストから取得できる") {
          val hasLoginState = result.right
            .map(
              _.token.token
            )
            .flatMap(
              appContext.contextOf(_).toRight(fail("context not found"))
            )
            .exists(
              _.loginContext.authNStatus.isAuthed
            )
          assert(hasLoginState)
        }
      }
      describe("パスワードが正しくない場合") {
        it("AuthNから認証に失敗したメッセージが取得できる") {}
        it("ログインコンテキストからはAuthNが取得できない") {}
        it("リトライ回数が増える") {}
        describe("リトライ回数が制限より大きい場合") {
          it("一時的にリトライを禁止する") {}
        }
      }
    }
    describe("メールアドレスが形式として間違っている場合") {
      val result =
        new EmailAuth(AuthEmailAddress("test_test.jp"), "rightPassword")
          .tryAuth()
      it("MailAuthからメールアドレス形式違反メッセージを取得できる") {
        val code = 100002
        val message = "Wrong email address"
        assert(result.isLeft)
        result.fold(
          _.code.describe,
          a => ""
        ) shouldBe s"[AuthNError:$code]$message"
      }
    }
  }
}
