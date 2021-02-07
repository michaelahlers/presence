package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.{ FocusedResumePage, ResumePage, UnfocusedResumePage }
import cats.syntax.option._
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.svg.G
import scala.util.Random

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceNodeView {

  def render(
    index: ExperienceNodeIndex,
    state: ExperienceNodeState,
    $state: Signal[ExperienceNodeState],
    $focusedExperienceId: Signal[Option[ExperienceId]]
  ): ReactiveSvgElement[G] = {
    import svg._

    /** @todo Will be parameterized, set by significance of an experience. */
    val $radius: Val[Int] = Val(20)

    val $uiState: Signal[ResumePage] =
      $state
        .toSignal(state)
        .map(_.id
          .map(FocusedResumePage(_))
          .getOrElse(UnfocusedResumePage))

    val $logo = $state.toSignal(state).map(_.logo)

    val $cx = $state.toSignal(state).map(_.cx)
    val $cy = $state.toSignal(state).map(_.cy)
    val $x = $cx.combineWith($radius).map { case (x, radius) => x - radius }
    val $y = $cy.combineWith($radius).map { case (y, radius) => y - radius }

    val $width = $radius.map(_ * 2)
    val $height = $radius.map(_ * 2)

    val clickBus: EventBus[MouseEvent] = new EventBus()

    val $revealed =
      EventStream
        .fromValue(true, emitOnce = true)
        .delay((index.toInt + 1) * (50 + Random.nextInt(50)))
        .toSignal(false)

    g(
      className := "experience-node-view",
      className.toggle("hidden") <-- $revealed.map(!_),
      className.toggle("revealed") <-- $revealed,
      child <-- $logo.map {
        case None =>
          circle(
            cx <-- $cx.map(_.toString),
            cy <-- $cy.map(_.toString),
            r <-- $radius.map(_.toString),
            fill := "#292929"
          )
        case Some(logo) =>
          image(
            xlinkHref := logo,
            x <-- $x.map(_.toString),
            y <-- $y.map(_.toString),
            width <-- $width.map(_.toString),
            height <-- $height.map(_.toString))
      },
      onClick.stopPropagation --> clickBus.writer,
      clickBus.events.withCurrentValueOf($uiState).map { case (_, x) => x } --> (UiState.router.pushState(_))
    )
  }

}
