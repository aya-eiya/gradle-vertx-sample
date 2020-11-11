package vchat.app.service.base

import graphql.schema.DataFetchingEnvironment
import io.vertx.scala.ext.web.RoutingContext
import vchat.state.api.ApplicationContextManager
import vchat.state.models.AccessContext
import vchat.state.models.values.AccessToken

object UseWebApplicationContext {
  def accessTokenHeaderName = "Access-Token"
}

trait UseWebApplicationContext {
  import UseWebApplicationContext._
  def contextManager: ApplicationContextManager

  def createToken: AccessToken = {
    val newToken = contextManager.createAccessToken
    contextManager.createApplicationContext(newToken)
    newToken
  }

  def getToken(
      context: RoutingContext
  ): Option[AccessToken] =
    for {
      c <-
        context
          .request()
          .headers()
          .get(accessTokenHeaderName)
      t = AccessToken(c)
      v <- contextManager.getApplicationContext(t)
      _ <- v.get[AccessContext]
    } yield t
}

trait UseGraphQLApplicationContext extends UseWebApplicationContext {
  def getToken(
      env: DataFetchingEnvironment
  ): Option[AccessToken] = getToken(env.getContext[RoutingContext])
}
