package ahlers.presence.web.client.resume

import ahlers.presence.experiences.{ Experience, ExperienceKey, ExperienceName }
import ahlers.presence.web.client.resume.ExperienceBriefState.Mode._
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.G

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBriefGlanceView {

  def render(
    index: ExperienceBriefIndex,
    state: ExperienceBriefState,
    $state: Signal[ExperienceBriefState],
    $focusedExperience: Signal[Option[Experience]],
    $glancedExperienceKeys: Signal[Set[ExperienceKey]],
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {

    /** Must be declared prior to importing from [[svg]]. */
    def renderLabel(name: ExperienceName): Label =
      label(
        xmlns("http://www.w3.org/1999/xhtml"),
        className("rounded-pill"),
        name.toText)

    import svg._

    val $isGlanced: Signal[Boolean] =
      $focusedExperience
        .combineWith($glancedExperienceKeys)
        .combineWith($state)
        .map {

          case (focusedExperience, glancedExperienceKeys, ExperienceBriefState(_, Content(experience), _, _, _)) =>
            !focusedExperience.map(_.key).contains(experience.key) &&
              glancedExperienceKeys.contains(experience.key)

          case _ =>
            false

        }

    g(
      className("experience-brief-glance-view"),
      className.toggle("glanced") <-- $isGlanced,
      children <--
        $state.map(state =>
          state.mode match {

            case Blank => ???

            case Content(experience) =>
              Seq(
                foreignObject(
                  x((state.x - 5).toString),
                  y((state.y - 5).toString),
                  width("100%"),
                  height((state.height + 10).toString),
                  renderLabel(experience.brief.name)),
                image(
                  xlinkHref(experience.brief.logo.toText),
                  x(state.x.toString),
                  y(state.y.toString),
                  width(state.width.toString),
                  height(state.height.toString))
              )

          }),
      modifiers
    )
  }

}
