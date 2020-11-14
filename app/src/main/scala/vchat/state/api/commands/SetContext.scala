package vchat.state.api.commands

import cats.data.OptionT
import cats.effect.IO
import vchat.state.models.Context
import vchat.state.models.values.SessionID
import vchat.state.repositories.ApplicationContextRepository

import scala.reflect.ClassTag

trait SetContext {

  val appContextRepo: ApplicationContextRepository
  def setContext[T <: Context: ClassTag](
      sessionID: SessionID,
      context: T
  ): OptionT[IO, Unit] = appContextRepo.putContext(sessionID, context)

}
