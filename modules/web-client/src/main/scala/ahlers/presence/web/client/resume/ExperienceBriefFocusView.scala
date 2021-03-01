package ahlers.presence.web.client.resume

import ahlers.presence.experiences.{ Experience, ExperienceKey }
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
    $focusedExperience: Signal[Option[Experience]],
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    val $isFocused: Signal[Boolean] =
      $focusedExperience.combineWith($state)
        .map {
          case (None, _) => false
          case (Some(focusedExperience), state) =>
            (focusedExperience.adjacents.map(_.key) + focusedExperience.key)
              .exists(state.key.contains(_))
        }

    val $isAdjacent: Signal[Boolean] =
      $focusedExperience.combineWith($state)
        .map {
          case (None, _) => false
          case (Some(focusedExperience), state) =>
            focusedExperience.adjacents.map(_.key)
              .exists(state.key.contains(_))
        }

    g(
      className("experience-brief-focus-view"),
      className.toggle("focused") <-- $isFocused,
      className.toggle("adjacent") <-- $isAdjacent,
      circle(
        cx(state.cx.toString),
        cy(state.cy.toString),
        r((state.r + 2).toString),
        fill("#f8f8f8")),
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
