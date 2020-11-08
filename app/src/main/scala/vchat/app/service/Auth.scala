package vchat.app.service

import graphql.GraphQL
import graphql.schema.DataFetchingEnvironment
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
import vchat.auth.domain.models.values.email.AuthEmailAddress

case class EmailAuthInput(
    rawEmailAddress: String,
    rawPassword: String
) {
  def emailAddress: AuthEmailAddress = AuthEmailAddress(rawEmailAddress)
}

object Auth {
  def verticleName: String = nameForVerticle[Auth]
  def schema: String =
    """
      |input EmailAuthInput {
      |  emailAddress: String
      |  rawPassword: String
      |}
      |
      |type Query {
      |  exists(input: EmailAuthInput): Boolean
      |}
      |""".stripMargin
}

class Auth extends Service with GraphQLMixIn {
  import vchat.app.service.Auth._
  import scala.collection.JavaConverters._

  def authorizer: StaticEmailAuthorizer.type = StaticEmailAuthorizer

  def exists(emailAuthInput: EmailAuthInput): Boolean =
    authorizer.emailRepo
      .exists(emailAuthInput.emailAddress, emailAuthInput.rawPassword)

  private def dataFetch(env: DataFetchingEnvironment): Boolean =
    Option(env.getArgument[java.util.Map[String, String]]("input"))
      .map(_.asScala)
      .flatMap(input =>
        (input.get("emailAddress"), input.get("rawPassword")) match {
          case (Some(address), Some(pass)) =>
            Some(EmailAuthInput(address, pass))
          case _ => None
        }
      )
      .exists(exists)

  override def graphQLHandler: GraphQL = {
    val parser = new SchemaParser()
    val reg: TypeDefinitionRegistry = parser.parse(schema)
    val gen = new SchemaGenerator

    val vdf = new VertxDataFetcher[Boolean]((env, p) =>
      p.complete(dataFetch(env))
    )
    val wiring = newRuntimeWiring
      .`type`(
        "Query",
        (builder: TypeRuntimeWiring.Builder) =>
          builder.dataFetcher(
            "exists",
            vdf
          )
      )
      .build
    GraphQL.newGraphQL(gen.makeExecutableSchema(reg, wiring)).build()
  }
}
