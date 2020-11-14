package vchat.app.service.auth.email.error

import vchat.logging.models.ErrorDescription

private[email] trait ErrorDescriptions {
  private[email] def dataNotFound: ErrorDescription =
    ErrorDescription(
      reason = "送信データが取得できませんでした",
      todo = "データの送信方法が間違っている可能性があります",
      reference = ""
    )
  private[email] def emailAddressNotFound: ErrorDescription =
    ErrorDescription(
      reason = "emailAddressが入力されていません",
      todo = "emailAddressは必須です",
      reference = ""
    )
  private[email] def passwordNotFound: ErrorDescription =
    ErrorDescription(
      reason = "Passwordが入力されていません",
      todo = "Passwordは必須です",
      reference = ""
    )
  private[email] def emailAddressAndPasswordNotFound: ErrorDescription =
    ErrorDescription(
      reason = "emailAddressとpasswordが入力されていません",
      todo = "emailAddressとpasswordは必須です",
      reference = ""
    )
  private[email] def invalidSessionID: ErrorDescription =
    ErrorDescription(
      reason = "sessionIDが正しくありません",
      todo = "Session-Idヘッダーに有効なトークンを指定してください",
      reference = ""
    )
  private[email] def failedToSetContext: ErrorDescription =
    ErrorDescription(
      reason = "情報の保存に失敗しました",
      todo = "システムの管理者に連絡してください",
      reference = ""
    )
}
