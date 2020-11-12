package vchat.state.models

import cats.data.OptionT
import cats.effect.IO

import scala.reflect.ClassTag

case class ApplicationContext(childContexts: Seq[_ <: Context])
    extends RootContext {
  def addAll(appends: Seq[_ <: Context]): IO[ApplicationContext] =
    for {
      a <- IO(appends)
      b = childContexts.filterNot(c => a.exists(_.getClass == c.getClass))
      c = b ++ a
    } yield ApplicationContext(c)

  def putContext(context: Context): IO[ApplicationContext] =
    addAll(Seq(context))

  def get[T <: Context: ClassTag]: OptionT[IO, T] =
    OptionT(
      IO(
        childContexts
          .filter(implicitly[ClassTag[T]].runtimeClass.isInstance(_))
          .map(_.asInstanceOf[T])
          .headOption
      )
    )
}
