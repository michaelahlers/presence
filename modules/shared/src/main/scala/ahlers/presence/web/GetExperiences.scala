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

  implicit private val codecExperienceName: Codec[ExperienceName] =
    deriveUnwrappedCodec

  implicit private val codecExperienceKey: Codec[ExperienceKey] =
    deriveUnwrappedCodec

  implicit private val codecExperienceAdjacent: Codec[ExperienceAdjacent] =
    deriveConfiguredCodec

  implicit private val codecExperienceCommentary: Codec[ExperienceCommentary] =
    deriveUnwrappedCodec

  implicit private val encoderExperienceLogo: Encoder[ExperienceLogo] =
    Encoder[String].contramap(_.toText)

  implicit private val decoderExperienceLogo: Decoder[ExperienceLogo] =
    Decoder[String].emapTry(ExperienceLogo.parseTry(_))

  implicit private val codecExperienceBrief: Codec[ExperienceBrief] =
    deriveConfiguredCodec

  implicit private val codecExperienceDetail: Codec[ExperienceDetail] = {
    implicit val defaultGenericConfiguration = Configuration.default.withDiscriminator("kind")
    deriveConfiguredCodec
  }

  implicit private val codecExperience: Codec[Experience] =
    deriveConfiguredCodec

  implicit val codecGetExperiencesResponse: Codec[GetExperiencesResponse] =
    deriveConfiguredCodec

}
