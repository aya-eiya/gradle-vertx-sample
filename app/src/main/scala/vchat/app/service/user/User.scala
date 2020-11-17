package vchat.app.service.user

import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.http.{HttpServerRequest, HttpServerResponse}
import vchat.app.env.AppEnvMap
import vchat.server.Service
import vchat.server.rest.RESTMixIn

object User {
  def verticleName: String = nameForVerticle[User]
}

class User extends Service with AppEnvMap with RESTMixIn {
  override def restHandler(
      req: HttpServerRequest,
      res: HttpServerResponse
  ): Unit =
    res.`end`("User OK")
}
