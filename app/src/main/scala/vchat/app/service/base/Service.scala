package vchat.app.service.base

import graphql.GraphQL
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod._
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.http.{HttpServerRequest, HttpServerResponse}
import io.vertx.scala.ext.web.handler.LoggerHandler
import io.vertx.scala.ext.web.{Router, RoutingContext}
import vchat.app.env.AppEnv
import vchat.app.service.base.GraphQLHandler.ContextFactory

import scala.concurrent.Future

trait RESTMixIn {
  def restHandler(req: HttpServerRequest, res: HttpServerResponse)
  def handler: Handler[RoutingContext] =
    (context: RoutingContext) =>
      restHandler(
        context.request(),
        context.response()
      )
}

trait GraphQLMixIn {
  def graphQLHandler: GraphQL
  def contextFactory: ContextFactory = { rc: RoutingContext =>
    println(rc.request().headers().toString)
    rc
  }

  def handler: Handler[RoutingContext] =
    GraphQLHandler
      .create(graphQLHandler)
      .queryContext(contextFactory)
}

abstract class Service extends ScalaVerticle with AppEnv {

  def logger: LoggerHandler = LoggerHandler.create()

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
        case _       => router.get
      }
      .foreach(_.handler(logger).handler(handler))

    vertx
      .createHttpServer()
      .requestHandler(router.accept)
      .listenFuture(port, host)
      .map(_ => ())
  }
}
