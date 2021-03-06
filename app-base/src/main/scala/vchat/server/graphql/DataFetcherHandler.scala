package vchat.server.graphql

import cats.data.EitherT
import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import graphql.schema.DataFetchingEnvironment
import io.vertx.core.Promise
import io.vertx.ext.web.handler.graphql.VertxDataFetcher
import vchat.logging.models.ErrorStatus

trait DataFetcherHandler[T] extends LazyLogging {
  import logger._

  protected def handler(
      env: DataFetchingEnvironment,
      p: Promise[T]
  ): EitherT[IO, _ <: ErrorStatus, T]

  def build: VertxDataFetcher[T] =
    new VertxDataFetcher[T]((env, p) => {
      handler(env, p)
        .transform {
          case Right(success)    => Right(p.complete(success))
          case Left(errorStatus) => Left(p.fail(errorStatus.toString))
        }
        .value
        .unsafeRunAsync {
          case Right(value)    => info(s"success:$value")
          case Left(throwable) => error("data fetch failed", throwable)
        }
    })
}
