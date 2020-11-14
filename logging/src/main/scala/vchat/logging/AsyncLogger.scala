package vchat.logging

import cats.effect.IO
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import vchat.logging.exception.AppException
import vchat.logging.models.ErrorStatus

trait AsyncLogger {
  @transient
  protected lazy val innerLogger: Logger =
    Logger(LoggerFactory.getLogger(getClass.getName))

  val logger: AsyncLogger = this

  def info(message: String): IO[Unit] =
    IO(
      innerLogger.info(message)
    )
  def warn(message: String): IO[Unit] =
    IO(
      innerLogger.info(message)
    )
  def warn(exception: AppException): IO[Unit] =
    IO(
      innerLogger.warn("a controlled exception reported", exception)
    )
  def debug(message: String): IO[Unit] =
    IO(
      innerLogger.debug(message)
    )
  def error(message: String): IO[Unit] =
    IO(
      innerLogger.error(message)
    )
  def error(errorStatus: ErrorStatus): IO[Unit] = error(errorStatus.toString)
  def error(throwable: Throwable): IO[Unit] =
    IO(
      innerLogger.error("An exception reported", throwable)
    )
  def error(obj: Any): IO[Unit] = {
    obj match {
      case null         => error("error reported: null")
      case e: Throwable => error(e)
      case e            => error(e.toString)
    }
  }

}
