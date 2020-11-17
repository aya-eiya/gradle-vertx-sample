package vchat.server.graphql.error

import vchat.logging.models.ErrorDescription

object ErrorDescriptions {
  private[graphql] def genericError: ErrorDescription =
    ErrorDescription(
      reason = "クエリの処理中にエラーが発生しました",
      todo = "システムの管理者に連絡してください",
      reference = ""
    )
}
