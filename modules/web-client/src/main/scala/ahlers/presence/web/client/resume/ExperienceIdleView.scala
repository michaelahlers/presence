package ahlers.presence.web.client.resume

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

    g(
      className := Seq("experience-idle-view", nodeState.kind),
      style := "--revealing-transition-delay: %dms".format(nodeState.index.toInt * 10),
      image(
        xlinkHref := nodeState.logo.getOrElse(???),
        x := nodeState.x.toString,
        y := nodeState.y.toString,
        width := nodeState.width.toString,
        height := nodeState.height.toString
      ),
      modifiers
    )
  }

}
