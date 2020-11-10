package vchat.state.api.commands

import vchat.state.models.Context
import vchat.state.models.values.AccessToken
import vchat.state.repositories.ApplicationContextRepository

import scala.reflect.ClassTag

trait SetContext {

  val appContextRepo: ApplicationContextRepository
  def setContext[T <: Context: ClassTag](
      accessToken: AccessToken,
      context: T
  ): Unit = appContextRepo.putContext(accessToken, context)

}
