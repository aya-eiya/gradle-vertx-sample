package vchat.state.api.commands

import cats.effect.IO
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

trait CreateApplicationContext {

  val appContextRepo: ApplicationContextRepository
  def createApplicationContext(accessToken: AccessToken): IO[Unit] =
    appContextRepo.create(accessToken)
}
