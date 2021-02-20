package ahlers.presence.web.client.resume

import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.G

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBriefIdleView {

  def render(
    briefState: ExperienceBriefState.Brief,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    g(
      className := Seq("experience-idle-view"),
      style := "--revealing-transition-delay: %dms".format(briefState.index.toInt * 10),
      image(
        xlinkHref := briefState.logo,
        x := briefState.x.toString,
        y := briefState.y.toString,
        width := briefState.width.toString,
        height := briefState.height.toString
      ),
      modifiers
    )
  }

}
