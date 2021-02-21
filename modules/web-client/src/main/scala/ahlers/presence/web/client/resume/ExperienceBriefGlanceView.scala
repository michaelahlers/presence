package ahlers.presence.web.client.resume

import ahlers.presence.web.client.resume.ExperienceDetail.{ Employment, Skill }
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes._
import org.scalajs.dom.svg._

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBriefGlanceView {

  def render(
    briefState: ExperienceBriefState.Brief,
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {

    /** Must be declared prior to importing from [[svg]]. */
    val labelRender: Label =
      label(
        xmlns := "http://www.w3.org/1999/xhtml",
        className := "rounded-pill",
        briefState.label
      )

    import svg._

    g(
      className := Seq("experience-glance-view"),
      foreignObject(
        x := (briefState.x - 5).toString,
        y := (briefState.y - 5).toString,
        width := "100%",
        height := (briefState.r * 2 + 10).toString,
        labelRender),
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
