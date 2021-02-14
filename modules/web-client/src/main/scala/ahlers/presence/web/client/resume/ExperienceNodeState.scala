package ahlers.presence.web.client.resume

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceNodeState(
  index: ExperienceNodeIndex,
  kind: String,
  id: Option[ExperienceId],
  logo: Option[String],
  cx: Double,
  cy: Double,
  radius: Double)

object ExperienceNodeState {

  implicit class Syntax(private val self: ExperienceNodeState) extends AnyVal {
    import self._

    def x: Double = cx - radius
    def y: Double = cy - radius

    def width: Double = radius * 2
    def height: Double = radius * 2

  }

}
