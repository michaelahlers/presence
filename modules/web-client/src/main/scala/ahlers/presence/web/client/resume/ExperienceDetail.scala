package ahlers.presence.web.client.resume

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceDetail {
  def id: String
}

object ExperienceDetail {

  case class Skill(
    id: String,
    name: String,
    relatesTo: Set[String])
    extends ExperienceDetail

  object Skill {}

  def skill(
    id: String,
    name: String,
    relatesTo: String*
  ): Skill =
    Skill(
      id,
      name,
      relatesTo.toSet)

  case class Employment(
    id: String,
    company: Employment.Company,
    relatesTo: Set[String])
    extends ExperienceDetail

  object Employment {
    case class Company(
      name: String,
      city: String,
      state: String)
  }

  def employment(
    id: String,
    company: Employment.Company,
    relatesTo: String*
  ): Employment =
    Employment(
      id,
      company,
      relatesTo.toSet)

}
