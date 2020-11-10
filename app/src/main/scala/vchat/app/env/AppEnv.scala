package vchat.app.env

import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpMethod._
import vchat.app.service._

private[env] object AppEnv {
  type Allowed = Set[HttpMethod]
  private val env: Map[Class[_], Env] = Map(
    classOf[EmailAuth] -> Env(8081, "/auth", Set(POST)),
    classOf[Message] -> Env(8082, "/message", Set(GET)),
    classOf[User] -> Env(8083, "/user", Set(GET, POST, PUT, DELETE))
  )
}

trait AppEnv {
  import AppEnv._
  private def e = env(this.getClass)
  def port: Int = e.port
  def path: String = e.path
  def methods: Allowed = e.methods
  def host: String = e.host
}

private[env] case class Env(
    port: Int,
    path: String,
    methods: AppEnv.Allowed,
    host: String = "0.0.0.0"
)
