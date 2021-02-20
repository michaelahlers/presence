package ahlers.presence.web.client.resume

import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.G

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBlankView {

  def render(
    nodeState: ExperienceNodeState,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    g(
      className := Seq("experience-blank-view", nodeState.kind),
      style := "--revealing-transition-delay: %dms".format(nodeState.index.toInt * 10),
      circle(
        cx := nodeState.cx.toString,
        cy := nodeState.cy.toString,
        r := nodeState.radius.toString,
        fill := "#343434"),
      modifiers
    )
  }

}
