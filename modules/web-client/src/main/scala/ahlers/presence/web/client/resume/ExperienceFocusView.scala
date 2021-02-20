package ahlers.presence.web.client.resume

import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes._
import org.scalajs.dom.svg._

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceFocusView {

  def render(
    nodeState: ExperienceNodeState,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {

    ///** Must be declared prior to importing from [[svg]]. */
    //val labelRender: Label =
    //  label(
    //    xmlns := "http://www.w3.org/1999/xhtml",
    //    className := "rounded-pill",
    //    nodeState.label.getOrElse(???))

    import svg._

    g(
      className := Seq("experience-focus-view"),
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
