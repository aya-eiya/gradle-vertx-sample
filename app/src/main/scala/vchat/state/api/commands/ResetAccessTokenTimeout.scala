package vchat.state.api.commands

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

trait ResetAccessTokenTimeout {

  val appContextRepo: ApplicationContextRepository
  def resetAccessTokenTimeout(accessToken: AccessToken): OptionT[IO, Unit] =
    appContextRepo.resetAccessTokenTimeout(accessToken)
}
