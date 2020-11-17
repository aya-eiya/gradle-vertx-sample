package vchat.app.service.message

import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.http.{HttpServerRequest, HttpServerResponse}
import vchat.app.env.AppEnvMap
import vchat.server.Service
import vchat.server.rest.RESTMixIn

object Message {
  def verticleName: String = nameForVerticle[Message]
}

class Message extends Service with AppEnvMap with RESTMixIn {
  override def restHandler(
      req: HttpServerRequest,
      res: HttpServerResponse
  ): Unit =
    res.`end`("Message OK")
}
