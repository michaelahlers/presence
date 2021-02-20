package ahlers.presence.web.client.resume

import ahlers.presence.web.client.resume.ExperienceBrief.{ Employment, Skill }
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes._
import org.scalajs.dom.svg._

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceGlanceView {

  def render(
    nodeState: ExperienceNodeState.Brief,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {

    /** Must be declared prior to importing from [[svg]]. */
    val labelRender: Label =
      label(
        xmlns := "http://www.w3.org/1999/xhtml",
        className := "rounded-pill",
        nodeState.label
      )

    import svg._

    g(
      className := Seq("experience-glance-view"),
      foreignObject(
        x := (nodeState.x - 5).toString,
        y := (nodeState.y - 5).toString,
        width := "100%",
        height := (nodeState.r * 2 + 10).toString,
        labelRender),
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
