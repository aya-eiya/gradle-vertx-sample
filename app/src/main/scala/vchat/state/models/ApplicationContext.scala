package vchat.state.models

import scala.reflect.ClassTag

case class ApplicationContext(childContexts: Seq[_ <: Context])
    extends RootContext {
  def addAll(appends: Seq[_ <: Context]): ApplicationContext =
    ApplicationContext(
      childContexts.filterNot(c =>
        appends.exists(
          _.getClass == c.getClass
        )
      ) ++ appends
    )

  def put(context: Context): ApplicationContext = addAll(Seq(context))

  def get[T <: Context: ClassTag]: Option[T] =
    childContexts
      .filter(implicitly[ClassTag[T]].runtimeClass.isInstance(_))
      .map(_.asInstanceOf[T])
      .headOption
}
