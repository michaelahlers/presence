package ahlers.presence.web.client.resume

import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import d3v4.d3hierarchy.{ Hierarchy, Packed }
import org.scalajs.dom.svg.G

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBriefBlankView {

  def render(
    nodeState: ExperienceBriefState,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    g(
      className := Seq("experience-blank-view"),
      style := "--revealing-transition-delay: %dms".format(nodeState.index.toInt * 10),
      circle(
        cx := nodeState.cx.toString,
        cy := nodeState.cy.toString,
        r := nodeState.r.toString,
        fill := "#343434"),
      modifiers
    )
  }

}
