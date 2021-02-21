package ahlers.presence.web

import ahlers.presence.experiences.{ Experience, ExperienceKey }
import cats.syntax.apply._
import io.circe._
import io.circe.generic.extras._
import io.circe.generic.extras.semiauto._

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object GetExperiences {}

case class GetExperiencesResponse(
  records: Seq[Experience])

object GetExperiencesResponse {

  implicit val codecExperienceKey: Codec[ExperienceKey] =
    deriveUnwrappedCodec

  implicit val codecExperience: Codec[Experience] = {
    implicit val configuration = Configuration.default.withDiscriminator("kind")
    deriveConfiguredCodec
  }

  implicit val codecGetExperiencesResponse: Codec[GetExperiencesResponse] = {
    implicit val configuration = Configuration.default
    deriveConfiguredCodec
  }

}
