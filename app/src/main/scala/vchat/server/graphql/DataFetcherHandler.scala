package vchat.server.graphql

import cats.effect.IO
import graphql.schema.DataFetchingEnvironment
import io.vertx.core.Promise
import io.vertx.ext.web.handler.graphql.VertxDataFetcher

trait DataFetcherHandler[T] {

  protected def handler(env: DataFetchingEnvironment, p: Promise[T]): IO[_]

  def build: VertxDataFetcher[T] =
    new VertxDataFetcher[T]((env, p) => {
      handler(env, p)
        .unsafeRunAsync {
          case Right(value)    => println(value)
          case Left(throwable) => println(throwable)
        }
    })
}
