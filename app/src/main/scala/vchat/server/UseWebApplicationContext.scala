package vchat.server

import _root_.graphql.schema.DataFetchingEnvironment
import cats.data.OptionT
import cats.effect.IO
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

  def createToken: IO[AccessToken] =
    for {
      newToken <- contextManager.createAccessToken
      _ <- contextManager.createApplicationContext(newToken)
    } yield newToken

  def getToken(
      context: RoutingContext
  ): OptionT[IO, AccessToken] =
    for {
      c <- getHeaderToken(context)
      t = AccessToken(c)
      v <- contextManager.getApplicationContext(t)
      _ <- v.get[AccessContext]
    } yield t

  private def getHeaderToken(context: RoutingContext) =
    OptionT(
      IO(
        context
          .request()
          .headers()
          .get(accessTokenHeaderName)
      )
    )
}

trait UseGraphQLApplicationContext extends UseWebApplicationContext {
  def getToken(
      env: DataFetchingEnvironment
  ): OptionT[IO, AccessToken] = getToken(env.getContext[RoutingContext])
}
