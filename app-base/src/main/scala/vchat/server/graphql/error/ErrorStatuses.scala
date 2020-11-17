package vchat.server.graphql.error

object ErrorStatuses {
  def genericError: GraphQLHandleErrorStatus =
    GraphQLHandleErrorStatus(
      GraphQLHandleErrorStatus.graphQLError,
      ErrorDescriptions.genericError
    )
}
