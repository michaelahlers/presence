package ahlers.presence.web.client.resume

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceNodeState(
  index: ExperienceNodeIndex,
  id: Option[ExperienceId],
  logo: Option[String],
  cx: Double,
  cy: Double)
