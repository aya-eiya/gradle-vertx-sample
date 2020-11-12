package vchat.state.infra.memory

import cats.data.OptionT
import cats.effect.IO
import vchat.auth.domain.models.LoginContext
import vchat.auth.domain.models.values.AuthNStatus
import vchat.state.models.{AccessContext, ApplicationContext, Context}
import vchat.state.models.values.{AccessToken, TimeoutAccessTokenStatus}
import vchat.state.repositories.ApplicationContextRepository

import scala.reflect.ClassTag
import collection.JavaConverters._
import scala.collection.mutable.{Map => MutMap}
import java.util.concurrent.{ConcurrentHashMap => JCMap}

object InMemoryApplicationContextRepository
    extends ApplicationContextRepository {
  private val data: MutMap[AccessToken, ApplicationContext] =
    new JCMap[AccessToken, ApplicationContext]().asScala

  override def contextOf(
      accessToken: AccessToken
  ): OptionT[IO, ApplicationContext] =
    OptionT(IO(data.get(accessToken)))

  override def create(accessToken: AccessToken): IO[Unit] =
    for {
      t <- IO(accessToken)
      a = AccessContext(TimeoutAccessTokenStatus.create)
      l = LoginContext(t, AuthNStatus.empty(t))
      c = ApplicationContext(Seq(a, l))
    } yield data.put(t, c)

  override def putContext[T <: Context: ClassTag](
      accessToken: AccessToken,
      context: T
  ): OptionT[IO, Unit] =
    for {
      a <- OptionT.liftF(IO(accessToken))
      b <- contextOf(a)
      c <- b.get[AccessContext]
      d <- OptionT.liftF(b.putContext(context))
    } yield data.put(a, d)
}
