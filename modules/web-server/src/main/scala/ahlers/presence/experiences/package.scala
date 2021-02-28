package ahlers.presence

import better.files.Resource
import cats.syntax.apply._
import cats.syntax.either._
import io.circe._
import io.circe.generic.extras._
import io.circe.yaml.parser._
import io.circe.generic.extras.defaults._
import io.circe.generic.extras.semiauto._

import scala.util.Try

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
package object experiences {

  implicit private val codecExperienceName: Codec[ExperienceName] =
    deriveUnwrappedCodec

  implicit private val codecExperienceKey: Codec[ExperienceKey] =
    deriveUnwrappedCodec

  implicit private val codecExperienceCommentary: Codec[ExperienceCommentary] =
    deriveUnwrappedCodec

  implicit private val encoderExperienceKey: Encoder[ExperienceLogo] =
    Encoder[String].contramap(_.toText)

  implicit private val decoderExperienceKey: Decoder[ExperienceLogo] =
    Decoder[String].emapTry(ExperienceLogo.parseTry(_))

  implicit private val codecExperienceBrief: Codec[ExperienceBrief] =
    deriveConfiguredCodec

  implicit private val codecExperienceDetail: Codec[ExperienceDetail] = {
    implicit val defaultGenericConfiguration = Configuration.default.withDiscriminator("kind")
    deriveConfiguredCodec
  }

  implicit private val codecExperience: Codec[Experience] =
    deriveConfiguredCodec

  lazy val employments: Seq[Experience] =
    Try(Resource.my.getAsString("employments.yaml")).toEither
      .flatMap(parse(_))
      .flatMap(_.as[Seq[Experience]])
      .valueOr(throw _)

  lazy val skills: Seq[Experience] =
    Try(Resource.my.getAsString("skills.yaml")).toEither
      .flatMap(parse(_))
      .flatMap(_.as[Seq[Experience]])
      .valueOr(throw _)

  lazy val all: Seq[Experience] =
    employments ++ skills

}
