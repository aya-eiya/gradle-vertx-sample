package vchat.app.service.auth.email

import cats.effect.IO
import graphql.schema.DataFetchingEnvironment
import io.vertx.core.Promise
import vchat.server.graphql.DataFetcherHandler
import vchat.state.models.values.AccessToken

case class AccessTokenDataFetcher(
    getAvailableAccessToken: DataFetchingEnvironment => IO[AccessToken]
) extends DataFetcherHandler[String] {

  override protected def handler(
      env: DataFetchingEnvironment,
      p: Promise[String]
  ): IO[_] = getAvailableAccessToken(env)
}
