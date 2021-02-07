package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.FocusedResumePage
import cats.syntax.option._
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.G

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceNodeView {

  def render(
    id: ExperienceId,
    state: ExperienceNodeState,
    $state: Signal[ExperienceNodeState],
    $focusedExperienceId: Signal[Option[ExperienceId]]
  ): ReactiveSvgElement[G] = {
    import svg._

    /** @todo Will be parameterized, set by significance of an experience. */
    val radius: Double = 20d

    val $logo = $state.map(_.logo)
    val $x = $state.map(_.x - radius)
    val $y = $state.map(_.y - radius)
    val $width = Val(radius * 2d)
    val $height = Val(radius * 2d)

    val handleClick =
      onClick
        .stopPropagation
        .mapToValue(FocusedResumePage(id)) --> (UiState.router.pushState(_))

    g(
      className := "experience-node-view",
      image(
        xlinkHref <-- $logo,
        x <-- $x.map(_.toString),
        y <-- $y.map(_.toString),
        width <-- $width.map(_.toString),
        height <-- $height.map(_.toString)),
      handleClick
    )
  }

}
