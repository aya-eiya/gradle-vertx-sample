package vchat.server.graphql.error

import vchat.logging.models.ErrorDescription
import vchat.server.error.{ServiceErrorCode, ServiceErrorStatus}

case class GraphQLHandleErrorStatus(
    code: ServiceErrorCode,
    description: ErrorDescription
) extends ServiceErrorStatus

object GraphQLHandleErrorStatus {
  def graphQLError: ServiceErrorCode =
    ServiceErrorCode(
      code = 20001,
      message = "general error in graphQL"
    )
}
