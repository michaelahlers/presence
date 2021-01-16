package ahlers.presence.web.client.resume
import com.raquo.airstream.signal.Var
import d3.laminar.{ SimulationLinkRx, SimulationNodeRx }
import d3v4.Index

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
  indexVar: Var[Option[Index]],
  sourceVar: Var[ExperienceNodeUi],
  targetVar: Var[ExperienceNodeUi])
  extends SimulationLinkRx[ExperienceNodeUi, ExperienceNodeUi]

case class ExperienceNodeUi(
  indexVar: Var[Option[Index]],
  xVar: Var[Option[Double]],
  yVar: Var[Option[Double]],
  vxVar: Var[Option[Double]],
  vyVar: Var[Option[Double]],
  fxVar: Var[Option[Double]],
  fyVar: Var[Option[Double]],
  payloadVar: Var[ExperienceDescription])
  extends SimulationNodeRx[ExperienceDescription]
