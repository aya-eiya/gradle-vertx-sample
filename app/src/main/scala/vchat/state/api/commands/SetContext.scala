package vchat.state.api.commands

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.Context
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

import scala.reflect.ClassTag

trait SetContext {

  val appContextRepo: ApplicationContextRepository
  def setContext[T <: Context: ClassTag](
      accessToken: AccessToken,
      context: T
  ): OptionT[IO, Unit] = appContextRepo.putContext(accessToken, context)

}
