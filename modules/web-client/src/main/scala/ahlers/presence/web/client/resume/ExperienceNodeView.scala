package ahlers.presence.web.client.resume

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.svg._
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
    $state: Signal[ExperienceNodeState] //,
    //focusObserver: Observer[ExperienceNodeState]
  ): ReactiveSvgElement[G] = {
    val radius: Double = 20d

    g(
      className := "experience-node-view",
      image(
        xlinkHref <-- $state.map(_.logo),
        x <-- $state.map(_.x - radius).map(_.toString),
        y <-- $state.map(_.y - radius).map(_.toString),
        width := (radius * 2d).toString,
        height := (radius * 2d).toString
      )
    )
  }

}
