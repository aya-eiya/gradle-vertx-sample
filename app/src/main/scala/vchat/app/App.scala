package vchat.app

import io.vertx.scala.core.Vertx
import vchat.app.service.auth.EmailAuth
import vchat.app.service.message.Message
import vchat.app.service.user.User

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object App {
  val vertx = Vertx.vertx()
  def main(args: Array[String]): Unit =
    Seq(
      vertx.deployVerticleFuture(EmailAuth.verticleName),
      vertx.deployVerticleFuture(Message.verticleName),
      vertx.deployVerticleFuture(User.verticleName)
    ).foreach(_.onComplete {
      case Success(s) => println(s"Verticle id is: $s")
      case Failure(t) => t.printStackTrace()
    })
}
