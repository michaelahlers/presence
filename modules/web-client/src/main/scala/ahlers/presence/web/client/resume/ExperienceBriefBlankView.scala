package ahlers.presence.web.client.resume

import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.G

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBriefBlankView {

  def render(
    briefState: ExperienceBriefState,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    g(
      className := Seq("experience-blank-view"),
      style := "--revealing-transition-delay: %dms".format(briefState.index.toInt * 10),
      circle(
        cx := briefState.cx.toString,
        cy := briefState.cy.toString,
        r := briefState.r.toString,
        fill := "#343434"),
      modifiers
    )
  }

}
