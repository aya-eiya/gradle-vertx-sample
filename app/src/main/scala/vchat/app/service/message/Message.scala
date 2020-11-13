package vchat.app.service.message

import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.http.{HttpServerRequest, HttpServerResponse}
import vchat.server.{RESTMixIn, Service}

object Message {
  def verticleName: String = nameForVerticle[Message]
}

class Message extends Service with RESTMixIn {
  override def restHandler(
      req: HttpServerRequest,
      res: HttpServerResponse
  ): Unit =
    res.`end`("Message OK")
}
