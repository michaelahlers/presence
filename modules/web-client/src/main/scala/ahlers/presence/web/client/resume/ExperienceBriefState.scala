package ahlers.presence.web.client.resume
import cats.syntax.option

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceBriefState {

  def index: ExperienceBriefIndex
  def cx: Double
  def cy: Double
  def r: Double

}

object ExperienceBriefState {

  case object Root extends ExperienceBriefState {
    override def index = ???
    override def cx = ???
    override def cy = ???
    override def r = ???
  }

  case class Blank(
    index: ExperienceBriefIndex,
    cx: Double,
    cy: Double,
    r: Double)
    extends ExperienceBriefState

  case class Brief(
    index: ExperienceBriefIndex,
    cx: Double,
    cy: Double,
    r: Double,
    id: ExperienceId,
    label: String,
    logo: String)
    extends ExperienceBriefState

  implicit class Syntax(private val self: ExperienceBriefState) extends AnyVal {
    import self._

    def x: Double = cx - r
    def y: Double = cy - r

    def width: Double = r * 2
    def height: Double = r * 2

  }

}
