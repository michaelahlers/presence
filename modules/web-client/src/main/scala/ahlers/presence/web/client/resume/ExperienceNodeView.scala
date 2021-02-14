package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.{ FocusedResumePage, UnfocusedResumePage }
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.G

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceNodeView {

  def render(
    state: ExperienceNodeState
  ): ReactiveSvgElement[G] = {
    import svg._

    val onClickEnterFocus: Modifier[ReactiveSvgElement[G]] =
      onClick
        .stopPropagation
        .mapToValue(state.id
          .map(FocusedResumePage(_))
          .getOrElse(UnfocusedResumePage)) --> (UiState.router.pushState(_))

    g(
      className := "experience-node-view",
      state.logo match {
        case None =>
          circle(
            cx := state.cx.toString,
            cy := state.cy.toString,
            r := state.radius.toString,
            fill := "#292929"
          )
        case Some(logo: String) =>
          image(
            xlinkHref := logo,
            x := state.x.toString,
            y := state.y.toString,
            width := state.width.toString,
            height := state.height.toString
          )
      },
      onClickEnterFocus
    )
  }

}
