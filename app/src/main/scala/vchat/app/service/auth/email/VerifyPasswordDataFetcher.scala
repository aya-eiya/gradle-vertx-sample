package vchat.app.service.auth.email

import cats.effect.IO
import graphql.schema.DataFetchingEnvironment
import io.vertx.core.Promise
import vchat.app.service.auth.email.schema.GraphQLSchema.LoginStatusData
import vchat.server.graphql.DataFetcherHandler
import cats.data.EitherT
import vchat.auth.models.values.email.EmailAuthNErrorStatus

case class VerifyPasswordDataFetcher(
    login: DataFetchingEnvironment => EitherT[
      IO,
      EmailAuthNErrorStatus,
      LoginStatusData
    ]
) extends DataFetcherHandler[LoginStatusData] {

  def handler(
      env: DataFetchingEnvironment,
      p: Promise[LoginStatusData]
  ): EitherT[IO, EmailAuthNErrorStatus, LoginStatusData] =
    login(env)

}
