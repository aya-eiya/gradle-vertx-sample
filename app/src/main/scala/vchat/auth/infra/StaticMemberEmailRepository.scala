package vchat.auth.infra

import vchat.auth.repositories._

object StaticMemberEmailRepository extends MemberEmailRepository {
  val data: Map[AuthEmailAddress, EncryptedPassword] = Map()
}
