package vchat.server.graphql

import cats.data.OptionT
import cats.effect.IO
import graphql.schema.DataFetchingEnvironment
import io.vertx.scala.ext.web.RoutingContext
import vchat.server.UseWebApplicationContext
import vchat.state.models.values.SessionID

trait UseGraphQLApplicationContext extends UseWebApplicationContext {
  def getSessionID(
      env: DataFetchingEnvironment
  ): OptionT[IO, SessionID] = getSessionID(env.getContext[RoutingContext])
}
