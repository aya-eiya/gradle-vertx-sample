package vchat.state.api.commands

import vchat.state.models.values.AccessToken
import vchat.state.repositories.AccessTokenRepository

trait CreateAccessToken {

  val tokenRepo: AccessTokenRepository
  def createAccessToken: AccessToken = tokenRepo.create()
}
