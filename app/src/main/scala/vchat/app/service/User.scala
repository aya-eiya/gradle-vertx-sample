package vchat.app.service
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.http.{HttpServerRequest, HttpServerResponse}
import vchat.app.service.base.{RESTMixIn, Service}

object User {
  def verticleName: String = nameForVerticle[User]
}

class User extends Service with RESTMixIn {
  override def restHandler(
      req: HttpServerRequest,
      res: HttpServerResponse
  ): Unit =
    res.`end`("User OK")
}
