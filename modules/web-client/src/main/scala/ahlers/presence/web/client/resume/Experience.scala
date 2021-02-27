package ahlers.presence.web.client.resume

import cats.Eq
import cats.syntax.option._

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

sealed trait ExperienceBrief

object ExperienceBrief {

  case class Blank(
    radius: Double)
    extends ExperienceBrief

  case class Skill(
    id: ExperienceId,
    name: ExperienceName,
    logo: String,
    radius: Double)
    extends ExperienceBrief

  object Skill {}

  def skill(
    id: ExperienceId,
    name: ExperienceName,
    logo: String
  ): Skill =
    Skill(
      id,
      name,
      logo,
      20d)

  case class Employment(
    id: ExperienceId,
    company: Employment.Company,
    logo: String,
    radius: Double)
    extends ExperienceBrief

  object Employment {
    case class Company(
      name: String,
      shortName: Option[String],
      city: String,
      state: String)
  }

  def employment(
    id: ExperienceId,
    company: Employment.Company,
    logo: String
  ): Employment =
    Employment(
      id,
      company,
      logo,
      20d)

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
