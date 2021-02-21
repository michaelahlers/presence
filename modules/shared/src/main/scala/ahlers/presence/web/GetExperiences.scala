package ahlers.presence.web

import ahlers.presence.experiences._
import cats.syntax.apply._
import io.circe._
import io.circe.generic.extras._
import io.circe.generic.extras.defaults._
import io.circe.generic.extras.semiauto._

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object GetExperiences

case class GetExperiencesResponse(
  records: Seq[Experience])

object GetExperiencesResponse {

  implicit val codecExperienceName: Codec[ExperienceName] =
    deriveUnwrappedCodec

  implicit val codecExperienceKey: Codec[ExperienceKey] =
    deriveUnwrappedCodec

  implicit val encoderExperienceKey: Encoder[ExperienceLogo] =
    Encoder[String].contramap(_.toText)

  implicit val decoderExperienceKey: Decoder[ExperienceLogo] =
    Decoder[String].emapTry(ExperienceLogo.parseTry(_))

  implicit val codecExperienceBrief: Codec[ExperienceBrief] =
    deriveConfiguredCodec

  implicit val codecExperienceDetail: Codec[ExperienceDetail] = {
    implicit val defaultGenericConfiguration = Configuration.default.withDiscriminator("kind")
    deriveConfiguredCodec
  }

  implicit val codecExperience: Codec[Experience] =
    deriveConfiguredCodec

  implicit val codecGetExperiencesResponse: Codec[GetExperiencesResponse] =
    deriveConfiguredCodec

}
