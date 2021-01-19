package ahlers.presence.web.client.resume
import cats.Eq
import cats.syntax.option._
import cats.syntax.eq._
import com.raquo.airstream.signal.{ Signal, Var }
import d3.laminar.{ SimulationLinkRx, SimulationNodeRx }
import d3v4.Index

import scala.scalajs.js.JSConverters.JSRichOption
import scala.language.postfixOps

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceId(toText: String) extends AnyVal

object ExperienceId {

  implicit val eqExperienceId: Eq[ExperienceId] =
    Eq.by(_.toText)

}

case class ExperienceName(toText: String) extends AnyVal

sealed trait ExperienceRef {
  def id: ExperienceId
}

sealed trait ExperienceDescription //extends ExperienceRef

object ExperienceDescription {

  case object Blank extends ExperienceDescription

  case class Skill(
    id: ExperienceId,
    name: ExperienceName,
    logo: Option[String])
    extends ExperienceDescription

  object Skill {}

  def skill(
    id: ExperienceId,
    name: ExperienceName,
    logo: Option[String]
  ): Skill =
    Skill(
      id,
      name,
      logo)

  def skill(
    id: ExperienceId,
    name: ExperienceName,
    logo: String
  ): Skill =
    Skill(
      id,
      name,
      logo.some)

  def skill(
    id: ExperienceId,
    name: ExperienceName
  ): Skill =
    Skill(
      id,
      name,
      none)

  case class Employment(
    id: ExperienceId,
    company: Employment.Company,
    logo: Option[String])
    extends ExperienceDescription

  object Employment {
    case class Company(
      name: String,
      city: String,
      state: String)
  }

  def employment(
    id: ExperienceId,
    company: Employment.Company,
    logo: Option[String]
  ): Employment =
    Employment(
      id,
      company,
      logo)

  def employment(
    id: ExperienceId,
    company: Employment.Company,
    logo: String
  ): Employment =
    Employment(
      id,
      company,
      logo.some)

  def employment(
    id: ExperienceId,
    company: Employment.Company
  ): Employment =
    Employment(
      id,
      company,
      none)

}

//case class ExperienceLinkUi(
//  source: ExperienceNodeUi,
//  target: ExperienceNodeUi)
//  extends SimulationLinkRx[ExperienceNodeUi, ExperienceNodeUi] {
//
//  /** For method chaining. */
//  // TODO: Factor out syntax for sim. node.
//  def withIndex(index: Option[Index]): ExperienceLinkUi = {
//    this.index = index.orUndefined
//    this
//  }
//
//  def withIndex(index: Index): ExperienceLinkUi =
//    withIndex(index.some)
//
//}

//object ExperienceLinkUi {
//
//  implicit class Syntax(private val self: ExperienceLinkUi) extends AnyVal {
//    import self.source
//    import self.target
//    def contains(node: ExperienceNodeUi): Boolean =
//      (source == node) ||
//        (target == node)
//  }
//
//}

case class ExperienceNodeUi(
  index: Int,
  experience: ExperienceDescription)
