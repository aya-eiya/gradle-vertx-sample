package vchat.server.graphql.state

import cats.data.EitherT
import cats.effect.IO
import graphql.schema.DataFetchingEnvironment
import io.vertx.core.Promise
import vchat.logging.models.ErrorStatus
import vchat.server.graphql.DataFetcherHandler
import vchat.server.graphql.error.ErrorStatuses
import vchat.state.models.values.SessionID

case class SessionIDDataFetcher(
    getAvailableSessionID: DataFetchingEnvironment => IO[SessionID]
) extends DataFetcherHandler[String] {

  override protected def handler(
      env: DataFetchingEnvironment,
      p: Promise[String]
  ): EitherT[IO, ErrorStatus, String] =
    EitherT(getAvailableSessionID(env).attempt)
      .map(_.value)
      .leftMap(_ => ErrorStatuses.genericError)
}
