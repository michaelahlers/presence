package ahlers.presence.web.client.resume

import cats.syntax.option._
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.G
import ExperienceBriefState.Mode._
import ahlers.presence.experiences.{ Experience, ExperienceKey }

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceBriefView {

  def render(
    index: ExperienceBriefIndex,
    state: ExperienceBriefState,
    $state: Signal[ExperienceBriefState],
    focusedExperienceObserver: Observer[Option[ExperienceKey]],
    glancedExperienceKeysVar: Var[Set[ExperienceKey]],
    modifiers: Modifier[ReactiveSvgElement[G]]*
  ): ReactiveSvgElement[G] = {
    import svg._

    g(
      className("experience-brief-view"),
      className.toggle("blank") <-- $state.map(_.mode.isBlank),
      className.toggle("content") <-- $state.map(_.mode.isContent),
      style <-- $state.map(_.index.toInt * 10 + 10).map("--revealing-transition-delay: %dms".format(_)),
      child <-- $state.map(state =>
        state.mode match {

          case Blank =>
            circle(
              cx(state.cx.toString),
              cy(state.cy.toString),
              r(state.r.toString),
              fill("#343434"))

          case Content(experience) =>
            val onClickEnterFocus =
              onClick
                .stopPropagation
                .mapToStrict(experience.key.some)

            val onMouseEnterGlance =
              onMouseEnter
                .stopPropagation
                .mapToStrict(experience.key)

            val onMouseLeaveGlance =
              onMouseLeave
                .stopPropagation
                .mapToStrict(experience.key)

            image(
              xlinkHref(experience.brief.logo.toText),
              x(state.x.toString),
              y(state.y.toString),
              width(state.width.toString),
              height(state.height.toString),
              onClickEnterFocus --> focusedExperienceObserver,
              onMouseEnterGlance --> (key => glancedExperienceKeysVar.update(_ + key)),
              onMouseLeaveGlance --> (key => glancedExperienceKeysVar.update(_ - key))
            )

        }),
      modifiers
    )
  }

}
