package vchat.app.service.base

import java.util.{Locale, function}

import graphql.GraphQL
import io.vertx.core.Handler
import io.vertx.ext.web.handler.graphql.{
  GraphQLHandlerOptions,
  GraphQLHandler => JGraphQLHandler
}
import io.vertx.scala.ext.web.RoutingContext
import io.vertx.ext.web.{RoutingContext => JRoutingContext}
import org.dataloader.DataLoaderRegistry
import scala.language.implicitConversions

object GraphQLHandler {
  type ContextFactory = RoutingContext => AnyRef
  type DataLoaderFactory = RoutingContext => DataLoaderRegistry
  type LocaleFactory = RoutingContext => Locale
  type JContextFactory = Function[JRoutingContext, AnyRef]
  type JDataLoaderFactory = Function[JRoutingContext, DataLoaderRegistry]
  type JLocaleFactory = Function[JRoutingContext, Locale]

  private[base] implicit def javaHandler2ScalaHandler(
      handler: JGraphQLHandler
  ): GraphQLHandler = new GraphQLHandler(handler)
  implicit def jRoutingContext2RoutingContext(
      rc: JRoutingContext
  ): RoutingContext = new RoutingContext(rc)

  def create(graphQL: GraphQL): GraphQLHandler = JGraphQLHandler.create(graphQL)
  def create(graphQL: GraphQL, options: GraphQLHandlerOptions): GraphQLHandler =
    JGraphQLHandler.create(graphQL, options)
}

private[base] trait JGraphQLHandlerMethods {
  val innerHandler: JGraphQLHandler
  private[base] def innerQueryContext(
      factory: function.Function[JRoutingContext, AnyRef]
  ): JGraphQLHandler = innerHandler.queryContext(factory)
  private[base] def innerDataLoaderRegistry(
      factory: function.Function[JRoutingContext, DataLoaderRegistry]
  ): JGraphQLHandler = innerHandler.dataLoaderRegistry(factory)
  private[base] def innerLocale(
      factory: function.Function[JRoutingContext, Locale]
  ): JGraphQLHandler = innerHandler.locale(factory)
}
class GraphQLHandler(val innerHandler: JGraphQLHandler)
    extends Handler[RoutingContext]
    with JGraphQLHandlerMethods {
  import GraphQLHandler._

  override def handle(event: RoutingContext): Unit =
    innerHandler
      .queryContext(jRoutingContext2RoutingContext)
      .handle(event.asJava.asInstanceOf[JRoutingContext])

  def queryContext(factory: ContextFactory): GraphQLHandler = {
    val f: function.Function[JRoutingContext, AnyRef] = { rc: JRoutingContext =>
      factory.apply(rc)
    }
    innerQueryContext(f)
    this
  }

  def dataLoaderRegistry(factory: DataLoaderFactory): GraphQLHandler = {
    val f: function.Function[JRoutingContext, DataLoaderRegistry] = {
      rc: JRoutingContext => factory.apply(rc)
    }
    innerDataLoaderRegistry(f)
    this
  }

  def locale(factory: LocaleFactory): GraphQLHandler = {
    val f: function.Function[JRoutingContext, Locale] = { rc: JRoutingContext =>
      factory.apply(rc)
    }
    innerLocale(f)
    this
  }

}
