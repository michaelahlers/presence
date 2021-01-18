package ahlers.presence.web.client.resume
import cats.syntax.option._
import d3.laminar.{ SimulationLinkRx, SimulationNodeRx }
import d3v4.Index

import scala.scalajs.js.JSConverters.JSRichOption

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceId(toText: String) extends AnyVal

case class ExperienceName(toText: String) extends AnyVal

sealed trait ExperienceRef {
  def id: ExperienceId
}

sealed trait ExperienceDescription extends ExperienceRef

object ExperienceDescription {

  case class Skill(
    id: ExperienceId,
    name: ExperienceName)
    extends ExperienceDescription

  object Skill {}

  def skill(
    id: ExperienceId,
    name: ExperienceName
  ): Skill =
    Skill(
      id,
      name)

  case class Employment(
    id: ExperienceId,
    company: Employment.Company)
    extends ExperienceDescription

  object Employment {
    case class Company(
      name: String,
      city: String,
      state: String)
  }

  def employment(
    id: ExperienceId,
    company: Employment.Company
  ): Employment =
    Employment(
      id,
      company)

}

case class ExperienceLinkUi(
  source: ExperienceNodeUi,
  target: ExperienceNodeUi)
  extends SimulationLinkRx[ExperienceNodeUi, ExperienceNodeUi] {

  /** For method chaining. */
  // TODO: Factor out syntax for sim. node.
  def withIndex(index: Option[Index]): ExperienceLinkUi = {
    this.index = index.orUndefined
    this
  }

  def withIndex(index: Index): ExperienceLinkUi =
    withIndex(index.some)

}

case class ExperienceNodeUi(
  description: ExperienceDescription)
  extends SimulationNodeRx {

  /** For method chaining. */
  // TODO: Factor out syntax for sim. node.
  def withIndex(index: Option[Index]): ExperienceNodeUi = {
    this.index = index.orUndefined
    this
  }

  def withIndex(index: Index): ExperienceNodeUi =
    withIndex(index.some)

}
