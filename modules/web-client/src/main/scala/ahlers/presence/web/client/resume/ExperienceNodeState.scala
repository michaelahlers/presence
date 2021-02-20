package ahlers.presence.web.client.resume
import cats.syntax.option

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceNodeState {

  def index: ExperienceNodeIndex
  def cx: Double
  def cy: Double
  def r: Double

}

object ExperienceNodeState {

  case object Root extends ExperienceNodeState {
    override def index = ???
    override def cx = ???
    override def cy = ???
    override def r = ???
  }

  case class Blank(
    index: ExperienceNodeIndex,
    cx: Double,
    cy: Double,
    r: Double)
    extends ExperienceNodeState

  case class Brief(
    index: ExperienceNodeIndex,
    cx: Double,
    cy: Double,
    r: Double,
    id: ExperienceId,
    label: String,
    logo: String)
    extends ExperienceNodeState

  implicit class Syntax(private val self: ExperienceNodeState) extends AnyVal {
    import self._

    def x: Double = cx - r
    def y: Double = cy - r

    def width: Double = r * 2
    def height: Double = r * 2

  }

}
