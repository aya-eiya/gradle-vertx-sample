package vchat.auth.api.impl

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.matchers.should.Matchers
import vchat.auth.api.queries.GetEmailAuth
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus
}
import vchat.state.infra.memory.InMemoryApplicationContextRepository
import vchat.state.repositories.ApplicationContextRepository

class EmailAuthTest extends AsyncFunSpec with AsyncIOSpec with Matchers {
  val appContext: ApplicationContextRepository =
    InMemoryApplicationContextRepository

  describe("メールアドレスによる認証ができる") {
    describe("メールアドレスが形式として正しい場合") {
      describe("パスワードが正しい場合") {
        val result =
          EmailAuth(AuthEmailAddress("test@test.jp"), "rightPassword")
            .tryAuth()
        it("AuthNが認証済みになる") {
          result.value.asserting {
            case Right(status) =>
              assert(status.isAuthed)
            case Left(err) => fail(s"login failed: $err")
          }
        }
        it("認証済みのAuthNがログインコンテキストから取得できる") {
          (for {
            s <- result.leftMap(_.toString)
            token = s.token.token
            loginContext <- for {
              ac <-
                appContext
                  .contextOf(token)
                  .toRight(
                    s"application context($token) not found"
                  )
              lc <-
                ac.get[LoginContext]
                  .toRight(
                    s"login context($token) not found"
                  )
            } yield lc
          } yield loginContext.authNStatus).value.asserting {
            case Right(status) =>
              assert(status.isAuthed, s"token=${status.token}")
            case Left(err) => fail(s"auth not found: $err")
          }
        }
      }
      describe("パスワードが正しくない場合") {
        val result =
          EmailAuth(AuthEmailAddress("test@test.jp"), "wrongPassword")
            .tryAuth()
        it("AuthNから認証に失敗したメッセージが取得できる") {
          result.value.asserting {
            case Right(_) => fail("should not login")
            case Left(err) =>
              err.code shouldBe EmailAuthNErrorStatus.memberNotFound
              err.description shouldBe GetEmailAuth.ErrorDescriptions.verifyErrorDescription
          }
        }
        it("ログインコンテキストからはAuthNが取得できない") { IO(info("todo...")) }
        it("リトライ回数が増える") { IO(info("todo...")) }
        describe("リトライ回数が制限より大きい場合") {
          it("一時的にリトライを禁止する") { IO(info("todo...")) }
        }
      }
    }
    describe("メールアドレスが形式として間違っている場合") {
      val result = EmailAuth(AuthEmailAddress("test_test.jp"), "rightPassword")
        .tryAuth()
      it("MailAuthからメールアドレス形式違反メッセージを取得できる") {
        result.value.asserting {
          case Right(_) => fail("should not login")
          case Left(err) =>
            err.code shouldBe EmailAuthNErrorStatus.wrongEmailAddressErrorCode
            err.description shouldBe GetEmailAuth.ErrorDescriptions.wrongEmailAddressDescription
        }
      }
    }
  }
}
