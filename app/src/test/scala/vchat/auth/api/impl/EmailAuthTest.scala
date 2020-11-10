package vchat.auth.api.impl

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus
}
import vchat.state.infra.memory.InMemoryApplicationContextRepository
import vchat.state.repositories.ApplicationContextRepository

class EmailAuthTest extends AnyFunSpec with Matchers {
  val appContext: ApplicationContextRepository =
    InMemoryApplicationContextRepository

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
          (for {
            r <- result
            t = r.token.token
            ac <-
              appContext
                .contextOf(t)
                .toRight(fail(s"application context($t) not found"))
            lc <-
              ac.get[LoginContext].toRight(fail(s"login context($t) not found"))
            b = lc.authNStatus.isAuthed
          } yield b) match {
            case Right(hasLoginState) => assert(hasLoginState)
            case Left(err)            => fail(s"error $err")
          }
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
        val err = EmailAuthNErrorStatus.wrongEmailAddressErrorCode
        val code = err.code
        val message = err.message
        assert(result.isLeft)
        result.fold(
          _.code.describe,
          a => ""
        ) shouldBe s"[AuthNError:$code]$message"
      }
    }
  }
}
