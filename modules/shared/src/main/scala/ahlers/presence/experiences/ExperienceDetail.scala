package ahlers.presence.experiences

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceDetail {
  def logo: ExperienceLogo
  def name: ExperienceName
  def summary: ExperienceSummary
  def commentary: ExperienceCommentary
}
object ExperienceDetail {

  case class Employment(
    logo: ExperienceLogo,
    name: ExperienceName,
    summary: ExperienceSummary,
    commentary: ExperienceCommentary)
    extends ExperienceDetail

  case class Skill(
    logo: ExperienceLogo,
    name: ExperienceName,
    summary: ExperienceSummary,
    commentary: ExperienceCommentary)
    extends ExperienceDetail

}
