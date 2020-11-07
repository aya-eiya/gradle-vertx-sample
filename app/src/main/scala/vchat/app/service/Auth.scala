package vchat.app.service

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.{
  SchemaGenerator,
  SchemaParser,
  TypeDefinitionRegistry,
  TypeRuntimeWiring
}
import io.vertx.ext.web.handler.graphql.VertxDataFetcher
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import vchat.app.service.base._
import vchat.auth.api.impl.StaticEmailAuthorizer

case class EmailAuth(
    emailAddress: String,
    rawPassword: String
)
object Auth {
  def verticleName: String = nameForVerticle[Auth]
  def schema: String =
    """
      |type EmailAuth {
      |  emailAddress: String
      |  rawPassword: String
      |}
      |
      |type Query {
      |  emailAuths: [EmailAuth]
      |}
      |""".stripMargin
}

class Auth extends Service with GraphQLMixIn {
  import vchat.app.service.Auth._

  def authorizer: StaticEmailAuthorizer.type = StaticEmailAuthorizer

  override def graphQLHandler: GraphQL = {
    import java.util.{List => JList}
    import scala.collection.JavaConverters._

    val parser = new SchemaParser()
    val reg: TypeDefinitionRegistry = parser.parse(schema)
    val gen = new SchemaGenerator
    val df = new VertxDataFetcher[JList[EmailAuth]]({ (env, p) =>
      p.complete(List(EmailAuth("test@test.jp", "password_001")).asJava)
    })
    val wiring = newRuntimeWiring
      .`type`(
        "Query",
        (builder: TypeRuntimeWiring.Builder) =>
          builder.dataFetcher(
            "emailAuths",
            env => df
          )
      )
      .build
    GraphQL.newGraphQL(gen.makeExecutableSchema(reg, wiring)).build()
  }
}
