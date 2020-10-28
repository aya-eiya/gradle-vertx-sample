package vchat.auth.repositories

import org.scalatest.funspec.AnyFunSpec

class MailAuthTest extends AnyFunSpec {
  describe("メールアドレスによる認証ができる") {
    def testMailAuth = new MailAuth()
    describe("メールアドレスが形式として正しい場合") {
      describe("パスワードが正しい場合") {
        it("AuthNが認証済みになる") {

        }
        it("認証済みのAuthNがログインコンテキストから取得できる") {

        }
      }
      describe("パスワードが正しくない場合") {
        it("AuthNから認証に失敗したメッセージが取得できる") {

        }
        it("ログインコンテキストからはAuthNが取得できない") {

        }
        it("リトライ回数が増える") {

        }
        describe("リトライ回数が制限より大きい場合") {
          it("一時的にリトライを禁止する") {

          }
        }
      }
    }
    describe("メールアドレスが形式として間違っている場合") {
      it("MailAuthからメールアドレス形式違反メッセージを取得できる") {

      }
    }
  }
}
