package vchat.app

import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.scalalogging.LazyLogging
import io.vertx.scala.core.Vertx
import vchat.app.service.auth.EmailAuth
import vchat.app.service.message.Message
import vchat.app.service.user.User
import fs2.Stream

object App extends IOApp with LazyLogging {
  import logger._
  val vertx: Vertx = Vertx.vertx()
  val loggerOptions: List[String] = Nil

  def deploy(name: String): Stream[IO, String] =
    Stream.eval(IO.fromFuture(IO(vertx.deployVerticleFuture(name))))

  def vertxApps: IO[ExitCode] =
    (for {
      id1 <- deploy(EmailAuth.verticleName)
      id2 <- deploy(Message.verticleName)
      id3 <- deploy(User.verticleName)
    } yield {
      info(id1)
      info(id2)
      info(id3)
    }).compile.drain
      .as(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      apps <- vertxApps
    } yield apps
}
