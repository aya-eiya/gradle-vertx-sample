package vchat.server.graphql.state

import cats.data.EitherT
import cats.effect.IO
import graphql.schema.DataFetchingEnvironment
import io.vertx.core.Promise
import vchat.server.graphql.DataFetcherHandler
import vchat.state.models.values.SessionID

case class SessionIDDataFetcher(
    getAvailableSessionID: DataFetchingEnvironment => IO[SessionID]
) extends DataFetcherHandler[String] {

  override protected def handler(
      env: DataFetchingEnvironment,
      p: Promise[String]
  ): IO[Either[Unit, Unit]] =
    EitherT(getAvailableSessionID(env).attempt).transform {
      case Right(token) => Right(p.complete(token.value))
      case Left(err)    => Left(p.fail(err.toString))
    }.value
}
