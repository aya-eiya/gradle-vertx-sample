package vchat.app.service.auth.email

import cats.data.EitherT
import cats.effect.IO
import graphql.schema.DataFetchingEnvironment
import vchat.app.service.auth.EmailAuth.{
  emailAddressAndPasswordNotFoundStatus,
  emailAddressNotFoundStatus,
  failedToSetContextStatus,
  passwordNotFoundStatus
}
import vchat.auth.domain.models.values.email.{
  AuthEmailAddress,
  EmailAuthNErrorStatus
}

import scala.collection.JavaConverters.mapAsScalaMapConverter

object GraphQLSchema {

  case class EmailAuthInput(
      rawEmailAddress: String,
      rawPassword: String
  ) {
    def emailAddress: AuthEmailAddress = AuthEmailAddress(rawEmailAddress)
  }

  case class LoginStatusData(accessToken: String, authToken: String)

}

trait GraphQLSchema {
  import GraphQLSchema._

  def schema: String =
    """
      |type Status {
      |  accessToken: String!
      |  authToken: String
      |}
      |
      |input EmailAuthInput {
      |  emailAddress: String!
      |  rawPassword: String!
      |}
      |
      |type Query {
      |  accessToken: String!
      |  verifyPassword(input: EmailAuthInput): Status!
      |}
      |""".stripMargin

  def getInputValues(
      env: DataFetchingEnvironment
  ): EitherT[IO, EmailAuthNErrorStatus, Map[String, String]] =
    EitherT(
      IO(
        Option(env.getArgument[java.util.Map[String, String]]("input"))
          .toRight(failedToSetContextStatus)
          .map(_.asScala.toMap)
      )
    )

  def verifyInput(
      inputMap: Map[String, String]
  ): EitherT[IO, EmailAuthNErrorStatus, EmailAuthInput] =
    EitherT.fromEither(verifyInputMap(inputMap))

  def verifyInputMap(
      inputMap: Map[String, String]
  ): Either[EmailAuthNErrorStatus, EmailAuthInput] =
    (inputMap.get("emailAddress"), inputMap.get("rawPassword")) match {
      case (Some(address), Some(pass)) =>
        Right(EmailAuthInput(address, pass))
      case (Some(_), None) => emailAddressNotFoundStatus.toLeft
      case (None, Some(_)) => passwordNotFoundStatus.toLeft
      case (None, None)    => emailAddressAndPasswordNotFoundStatus.toLeft
    }
}
