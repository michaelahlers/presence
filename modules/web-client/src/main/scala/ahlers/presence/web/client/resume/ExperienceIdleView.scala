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
object ExperienceIdleView {

  def render(
    nodeState: ExperienceNodeState,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    val onClickEnterFocus: Modifier[ReactiveSvgElement[G]] =
      onClick
        .stopPropagation
        .mapToValue(nodeState.id
          .map(FocusedResumePage(_))
          .getOrElse(UnfocusedResumePage)) --> (UiState.router.pushState(_))

    g(
      className := Seq("experience-idle-view", nodeState.kind),
      style := "--revealing-transition-delay: %dms".format(nodeState.index.toInt * 10),
      nodeState.logo match {

        case None =>
          circle(
            cx := nodeState.cx.toString,
            cy := nodeState.cy.toString,
            r := nodeState.radius.toString,
            fill := "#292929"
          )

        case Some(logo: String) =>
          image(
            xlinkHref := logo,
            x := nodeState.x.toString,
            y := nodeState.y.toString,
            width := nodeState.width.toString,
            height := nodeState.height.toString)

      },
      onClickEnterFocus,
      modifiers
    )
  }

}
