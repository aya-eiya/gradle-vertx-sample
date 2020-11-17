package vchat.app.env

import io.vertx.core.http.HttpMethod

private[env] object AppEnv {
  type Allowed = Set[HttpMethod]
  type EnvMap = Class[_] => Env
  case class Env(
      port: Int,
      path: String,
      methods: AppEnv.Allowed,
      host: String = "0.0.0.0"
  )
}

trait AppEnv {
  import AppEnv._
  val envMap: EnvMap
  private def e: Env = envMap(this.getClass)
  final def port: Int = e.port
  final def path: String = e.path
  final def methods: Allowed = e.methods
  final def host: String = e.host
}
