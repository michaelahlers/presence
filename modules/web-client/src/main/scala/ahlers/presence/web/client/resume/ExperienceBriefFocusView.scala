package ahlers.presence.web.client.resume

import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes._
import org.scalajs.dom.svg._

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBriefFocusView {

  def render(
    nodeState: ExperienceBriefState.Brief,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    g(
      className := Seq("experience-focus-view"),
      image(
        xlinkHref := nodeState.logo,
        x := nodeState.x.toString,
        y := nodeState.y.toString,
        width := nodeState.width.toString,
        height := nodeState.height.toString
      ),
      modifiers
    )
  }

}
