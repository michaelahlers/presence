package ahlers.presence.web.client.resume

import cats.syntax.option._
import ahlers.presence.experiences.{ Experience, ExperienceKey, ExperienceLogo, ExperienceName }
import ahlers.presence.web.client.resume.ExperienceBriefState.Mode

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceBriefState(
  index: ExperienceBriefIndex,
  mode: Mode,
  cx: Double,
  cy: Double,
  r: Double)

object ExperienceBriefState {

  sealed abstract class Mode(
    val isRoot: Boolean = false,
    val isBlank: Boolean = false,
    val isContent: Boolean = false)

  object Mode {
    case object Root extends Mode(isRoot = true)
    case object Blank extends Mode(isBlank = true)
    case class Content(experience: Experience) extends Mode(isContent = true)
  }

  import Mode._

  implicit class Syntax(private val self: ExperienceBriefState) extends AnyVal {
    import self._

    def key: Option[ExperienceKey] =
      mode match {
        case Root | Blank => none
        case Content(experience) => experience.key.some
      }

    def x: Double = cx - r
    def y: Double = cy - r

    def width: Double = r * 2
    def height: Double = r * 2

  }

}
