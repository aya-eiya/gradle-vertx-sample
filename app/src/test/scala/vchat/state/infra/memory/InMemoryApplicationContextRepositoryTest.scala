package vchat.state.infra.memory

import cats.data.OptionT
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.matchers.should.Matchers
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.AuthToken
import vchat.auth.domain.models.values.email.EmailAuthNStatus
import vchat.state.models.AccessContext
import vchat.state.models.values.AccessToken

class InMemoryApplicationContextRepositoryTest
    extends AsyncFunSpec
    with AsyncIOSpec
    with Matchers {
  describe("インメモリマップを使用したApplicationContextの実装") {
    val repo = InMemoryApplicationContextRepository

    val key0 = AccessToken("unitTestToken_0")
    it("初期状態でデータが所得できないこと") {
      repo.contextOf(key0).value.asserting {
        _ shouldBe None
      }
    }
    describe("キーを一つ登録する場合について") {
      it("createを実行したあと初期状態の必須コンテキストが存在すること") {
        val f = for {
          _ <- OptionT.liftF(repo.create(key0))
          c <- repo.contextOf(key0)
          accessContext <- c.get[AccessContext]
          loginContext <- c.get[LoginContext]
          test = () => {
            assert(accessContext.status.existsAndNotExpired)
            loginContext.accessToken shouldBe key0
            assert(!loginContext.authNStatus.isAuthed)
          }
        } yield test
        f.value.asserting {
          case Some(test) => test()
          case None       => fail("context not found")
        }
      }
      it("登録したLoginContextを再取得できること") {
        val given = LoginContext(
          key0,
          EmailAuthNStatus(AuthToken(key0), isAuthed = true)
        )
        val f = for {
          _ <- repo.putContext(key0, given)
          c <- repo.contextOf(key0)
          loginContext <- c.get[LoginContext]
          test = () => {
            loginContext shouldBe given
          }
        } yield test
        f.value.asserting {
          case Some(test) => test()
          case None       => fail("context not found")
        }
      }
    }
    describe("2つ目のキーを登録する場合について") {
      val key1 = AccessToken("unitTestToken_1")
      it("createを実行したあと2つ目のキーで初期化されたコンテキストが取得できること") {
        val f = for {
          _ <- OptionT.liftF(repo.create(key1))
          c <- repo.contextOf(key1)
          accessContext <- c.get[AccessContext]
          loginContext <- c.get[LoginContext]
          test = () => {
            assert(accessContext.status.existsAndNotExpired)
            loginContext.accessToken shouldBe key1
            assert(!loginContext.authNStatus.isAuthed)
          }
        } yield test
        f.value.asserting {
          case Some(test) => test()
          case None       => fail("context not found")
        }
      }
      it("1つ目のキーで登録したコンテキストに変化がないこと") {
        val given = LoginContext(
          key0,
          EmailAuthNStatus(AuthToken(key0), isAuthed = true)
        )
        val f = for {
          c <- repo.contextOf(key0)
          loginContext <- c.get[LoginContext]
          test = () => {
            loginContext shouldBe given
          }
        } yield test
        f.value.asserting {
          case Some(test) => test()
          case None       => fail("context not found")
        }
      }
    }
  }
}
