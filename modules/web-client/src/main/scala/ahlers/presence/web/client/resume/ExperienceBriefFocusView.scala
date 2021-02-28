package ahlers.presence.web.client.resume

import ahlers.presence.experiences.ExperienceKey
import ahlers.presence.web.client.resume.ExperienceBriefState.Mode.{ Blank, Content }
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
    index: ExperienceBriefIndex,
    state: ExperienceBriefState,
    $state: Signal[ExperienceBriefState],
    $focusedExperienceKey: Signal[Option[ExperienceKey]],
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    val $isFocused: Signal[Boolean] =
      $focusedExperienceKey
        .combineWith($state)
        .map {
          case (glancedExperienceKeys, ExperienceBriefState(_, Content(experience), _, _, _)) =>
            glancedExperienceKeys.contains(experience.key)
          case _ =>
            false
        }

    g(
      className("experience-brief-focus-view"),
      className.toggle("focused") <-- $isFocused,
      child <--
        $state.map(state =>
          state.mode match {

            case Blank => ???

            case Content(experience) =>
              image(
                xlinkHref(experience.brief.logo.toText),
                x(state.x.toString),
                y(state.y.toString),
                width(state.width.toString),
                height(state.height.toString))

          }),
      modifiers
    )
  }

}
