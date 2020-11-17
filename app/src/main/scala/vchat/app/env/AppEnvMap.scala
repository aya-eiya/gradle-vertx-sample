package vchat.app.env

import io.vertx.core.http.HttpMethod.{DELETE, GET, POST, PUT}
import vchat.app.env.AppEnvMap.env
import vchat.app.service.auth.EmailAuth
import vchat.app.service.message.Message
import vchat.app.service.user.User

object AppEnvMap {
  private val env: Map[Class[_], Env] = Map(
    classOf[EmailAuth] -> Env(8081, "/auth", Set(POST)),
    classOf[Message] -> Env(8082, "/message", Set(GET)),
    classOf[User] -> Env(8083, "/user", Set(GET, POST, PUT, DELETE))
  )
}

trait AppEnvMap {
  implicit val envMap: Class[_] => Env = { clz: Class[_] => env(clz) }
}
