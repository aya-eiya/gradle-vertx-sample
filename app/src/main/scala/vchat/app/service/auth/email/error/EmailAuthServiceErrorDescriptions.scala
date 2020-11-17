package vchat.app.service.auth.email.error

import vchat.logging.models.ErrorDescription

private[error] trait EmailAuthServiceErrorDescriptions {
  protected def dataNotFound: ErrorDescription =
    ErrorDescription(
      reason = "送信データが取得できませんでした",
      todo = "データの送信方法が間違っている可能性があります",
      reference = ""
    )
  protected def emailAddressNotFound: ErrorDescription =
    ErrorDescription(
      reason = "emailAddressが入力されていません",
      todo = "emailAddressは必須です",
      reference = ""
    )
  protected def passwordNotFound: ErrorDescription =
    ErrorDescription(
      reason = "Passwordが入力されていません",
      todo = "Passwordは必須です",
      reference = ""
    )
  protected def emailAddressAndPasswordNotFound: ErrorDescription =
    ErrorDescription(
      reason = "emailAddressとpasswordが入力されていません",
      todo = "emailAddressとpasswordは必須です",
      reference = ""
    )
  protected def invalidSessionID: ErrorDescription =
    ErrorDescription(
      reason = "sessionIDが正しくありません",
      todo = "Session-Idヘッダーに有効なトークンを指定してください",
      reference = ""
    )
  protected def failedToSetContext: ErrorDescription =
    ErrorDescription(
      reason = "情報の保存に失敗しました",
      todo = "システムの管理者に連絡してください",
      reference = ""
    )
}
