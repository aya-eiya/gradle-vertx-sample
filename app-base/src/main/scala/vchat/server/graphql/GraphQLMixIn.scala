package vchat.server.graphql

import graphql.GraphQL
import io.vertx.core.Handler
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.RoutingContext

trait GraphQLMixIn extends ScalaVerticle {
  def graphQLHandler: GraphQL

  def handler: Handler[RoutingContext] =
    GraphQLHandler
      .create(graphQLHandler)
}
