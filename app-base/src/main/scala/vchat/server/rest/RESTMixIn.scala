package vchat.server.rest

import io.vertx.core.Handler
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.http.{HttpServerRequest, HttpServerResponse}
import io.vertx.scala.ext.web.RoutingContext

trait RESTMixIn extends ScalaVerticle {
  def restHandler(req: HttpServerRequest, res: HttpServerResponse): Unit
  def handler: Handler[RoutingContext] =
    (context: RoutingContext) =>
      restHandler(
        context.request(),
        context.response()
      )
}
