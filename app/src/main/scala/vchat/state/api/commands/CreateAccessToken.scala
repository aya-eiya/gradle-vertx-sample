package vchat.state.api.commands

import cats.effect.IO
import vchat.state.models.values.AccessToken
import vchat.state.repositories.AccessTokenRepository

trait CreateAccessToken {

  val tokenRepo: AccessTokenRepository
  def createAccessToken: IO[AccessToken] = tokenRepo.create()
}
