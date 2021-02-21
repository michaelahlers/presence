package ahlers.presence.experiences

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceDetail
object ExperienceDetail {

  case class Employment(
    logo: ExperienceLogo)
    extends ExperienceDetail

  case class Skill(
    logo: ExperienceLogo)
    extends ExperienceDetail

}
