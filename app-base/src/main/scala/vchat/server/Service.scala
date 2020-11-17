package vchat.server

import com.typesafe.scalalogging.LazyLogging
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod._
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.handler.LoggerHandler
import io.vertx.scala.ext.web.{Router, RoutingContext}
import vchat.app.env.AppEnv

import scala.concurrent.Future

abstract class Service extends ScalaVerticle with AppEnv with LazyLogging {
  import logger._
  def loggerHandler: LoggerHandler = LoggerHandler.create()

  def handler: Handler[RoutingContext]

  override def startFuture(): Future[_] = {
    val router = Router.router(vertx)
    methods
      .map {
        case HEAD    => router.head
        case OPTIONS => router.options
        case GET     => router.get
        case POST    => router.post
        case PUT     => router.put
        case DELETE  => router.delete
        case CONNECT => router.connect
        case PATCH   => router.patch
        case TRACE   => router.trace
        case _       => router.head
      }
      .foreach(_.handler(loggerHandler).handler(handler))

    vertx
      .createHttpServer()
      .requestHandler(router.accept)
      .listenFuture(port, host)
      .map { _ =>
        info(
          s"${this.getClass.getName} service started. Port($port) Allowed Methods[${methods.mkString(",")}]"
        )
      }
  }
}
