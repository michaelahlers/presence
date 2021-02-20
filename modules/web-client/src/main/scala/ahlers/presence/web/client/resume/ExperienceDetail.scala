package ahlers.presence.web.client.resume

/**
 * @since February 20, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceDetail {
  def id: ExperienceId
  def logo: String
}

object ExperienceDetail {

  case class Skill(
    id: ExperienceId,
    name: ExperienceName,
    logo: String)
    extends ExperienceDetail

  case class Employment(
    id: ExperienceId,
    company: Employment.Company,
    logo: String)
    extends ExperienceDetail

  object Employment {
    case class Company(
      name: String,
      shortName: Option[String],
      city: String,
      state: String)
  }

}
