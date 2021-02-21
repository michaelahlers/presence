package ahlers.presence.experiences

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait Experience
object Experience {

  case class Employment(
    key: ExperienceKey)
    extends Experience

  case class Skill(
    key: ExperienceKey)
    extends Experience

}
