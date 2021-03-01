package ahlers.presence.web.client.resume

import ahlers.presence.experiences.{ Experience, ExperienceKey }
import cats.syntax.apply._
import cats.syntax.option._
import com.raquo.airstream.core.Signal
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import d3.laminar.syntax.zoom._
import d3v4.{ d3, Circle, CircleImpl }
import d3v4.d3.{ Transform, ZoomBehavior }
import d3v4.d3hierarchy.Packed
import org.scalajs.dom
import org.scalajs.dom.svg.SVG

import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichIterableOnce

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperiencesGridView {

  /**
   * Represents a trivial finite-state machine.
   * @todo Document more thoroughly and formalize the transitions.
   */
  sealed trait Phase
  object Phase {
    case object Loading extends Phase
    case object Revealing extends Phase
    case object Presenting extends Phase
  }

  def briefStates(experiences: Seq[Experience]): Seq[ExperienceBriefState] = {
    val circles: js.Array[Circle[ExperienceBriefState] with Packed] =
      d3.packSiblings((experiences.map(_.some) ++ Seq.fill(500)(none))
        .zipWithIndex
        .map {
          case (None, index) =>
            ExperienceBriefState(ExperienceBriefIndex(index), ExperienceBriefState.Mode.Blank, 0, 0, 18d + Math.pow(index.toInt, 2) / 750)
          case (Some(experience), index) =>
            ExperienceBriefState(ExperienceBriefIndex(index), ExperienceBriefState.Mode.Content(experience), 0, 0, 20d)
        }
        .map(data => CircleImpl(data, data.r * 1.2d))
        .toJSArray)

    val enclosure = d3.packEnclose(circles.take(experiences.size))

    circles
      .toSeq
      .map(circle =>
        circle
          .data.get
          .copy(
            cx = circle.x.get - enclosure.x.get,
            cy = circle.y.get - enclosure.y.get))
  }

  def onPhaseZooming(
    zoomBehavior: ZoomBehavior[dom.EventTarget],
    phaseVar: Var[Phase],
    $focusedState: Signal[Option[ExperienceBriefState]]
  ): Modifier[SvgElement] = {
    import Phase._

    inContext { thisNode =>
      phaseVar.signal.combineWith($focusedState)
      /** @todo Implement tap syntax. */
        .map { case (phase, focusedState) =>
          dom.console.log("onPhaseZooming", "phase", phase.toString, "focusedState", focusedState.flatMap(_.key).map(_.toText).getOrElse("(none)"))
          (phase, focusedState)
        } --> {

        /** When [[Loading]], set a wide view, regardless any state. */
        case (Loading, _) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  thisNode.ref.clientWidth / 2,
                  thisNode.ref.clientHeight / 2)
                .scale(0.5d))

        /** When [[Revealing]] and ''not'' focused, slowly zoom to overview. */
        case (Revealing, None) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(3000d)
                .on("end", () => phaseVar.set(Presenting)),
              d3.zoomIdentity
                .translate(
                  thisNode.ref.clientWidth / 2,
                  thisNode.ref.clientHeight / 2)
                .scale(1d)
            )

        /** When [[Revealing]] and focused, slowly zoom to narrow view. */
        case (Revealing, Some(state)) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(3000d)
                .on("end", () => phaseVar.set(Presenting)),
              d3.zoomIdentity
                .translate(
                  thisNode.ref.clientWidth / 2,
                  thisNode.ref.clientHeight / 2)
                .scale(5d)
                .translate(
                  -state.cx,
                  -state.cy)
            )

        /** When [[Presenting]] and unfocused, where the user now has control, quickly zoom to overview. */
        case (Presenting, None) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(1000d),
              d3.zoomIdentity
                .translate(
                  thisNode.ref.clientWidth / 2,
                  thisNode.ref.clientHeight / 2)
                .scale(1d))

        /** When [[Presenting]] and unfocused, where the user now has control, quickly zoom to narrow view. */
        case (Presenting, Some(state)) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(1000d),
              d3.zoomIdentity
                .translate(
                  thisNode.ref.clientWidth / 2,
                  thisNode.ref.clientHeight / 2)
                .scale(5d)
                .translate(
                  -state.cx,
                  -state.cy)
            )

      }
    }
  }

  //def onResizeCentering(observer: Observer[Transform]): Modifier[SvgElement] =
  //  inContext { thisNode =>
  //    def transform =
  //      d3.zoomIdentity
  //        .translate(
  //          thisNode.ref.clientWidth / 2,
  //          thisNode.ref.clientHeight / 2)
  //
  //    windowEvents
  //      .onResize
  //      .mapTo(transform)
  //      .debounce(100)
  //      .toSignal(transform) --> observer
  //  }

  val onClickExitFocus =
    onClick
      .stopPropagation
      .mapToStrict(none)

  def render(
    $experiences: Signal[Option[Seq[Experience]]],
    $focusedExperience: Signal[Option[Experience]],
    focusedExperienceObserver: Observer[Option[ExperienceKey]]
  ): ReactiveSvgElement[SVG] = {
    import svg._
    import Phase._

    val zoomBehavior: ZoomBehavior[dom.EventTarget] =
      d3.zoom()
        .scaleExtent(js.Array(0.5d, 5d))

    val $states: Signal[Seq[ExperienceBriefState]] =
      $experiences
        .map {
          case None => Nil
          case Some(experiences) => briefStates(experiences)
        }

    val $focusedState: Signal[Option[ExperienceBriefState]] =
      $states.combineWith($focusedExperience)
        .mapN {
          case (states, Some(focusedExperience)) => states.find(_.key.contains(focusedExperience.key))
          case (_, _) => none
        }

    val phaseVar: Var[Phase] = Var(Loading)
    val $phase: Signal[Phase] = phaseVar.signal

    val glancedExperienceKeysVar: Var[Set[ExperienceKey]] = Var(Set.empty)

    /** Indicates if the viewer is hovering over any of the experiences, debounced to avoid rapid transitions between glancing and not. */
    val $isGlancing: Signal[Boolean] =
      glancedExperienceKeysVar.signal.map(_.nonEmpty)
        .changes
        .debounce(250)
        .toSignal(false)

    val $isFocusing: Signal[Boolean] =
      $focusedExperience.map(_.nonEmpty)
    //$focusedState.map(_.nonEmpty)

    val zoomingTransformVar: Var[Transform] =
      Var(d3.zoomIdentity)

    //val centeringTransformVar: Var[Transform] =
    //  Var(d3.zoomIdentity)

    svg(
      className("experience-grid-view", "bg-dark"),
      zoomBehavior --> zoomingTransformVar.writer.contramap(_.transform),
      //g(
      // transform <-- centeringTransformVar.signal.map(_.toString()),
      g(
        className.toggle("loading") <-- $phase.map(Loading == _),
        className.toggle("revealing") <-- $phase.map(Revealing == _),
        className.toggle("presenting") <-- $phase.map(Presenting == _),
        className.toggle("glancing") <-- $isGlancing,
        className.toggle("focusing") <-- $isFocusing,
        transform <-- zoomingTransformVar.signal.map(_.toString()),
        g(children <--
          $states
            .split(_.index)(ExperienceBriefView.render(_, _, _, focusedExperienceObserver, glancedExperienceKeysVar))),
        g(children <--
          $states
            .map(_.filter(_.mode.isContent))
            .split(_.index)(ExperienceBriefFocusView.render(_, _, _, $focusedExperience))),
        g(children <--
          $states
            .map(_.filter(_.mode.isContent))
            .split(_.index)(ExperienceBriefGlanceView.render(_, _, _, $focusedExperience, glancedExperienceKeysVar.signal))) /*,
        circle(
          r("10"),
          cx("0"),
          cy("0"),
          fill("white")
        )*/
      ),
      onPhaseZooming(zoomBehavior, phaseVar, $focusedState),
      onClickExitFocus --> focusedExperienceObserver,
      //onResizeCentering(centeringTransformVar.writer),
      //centeringTransformVar.signal --> (dom.console.debug("centeringTransform", _)),
      //zoomingTransformVar.signal --> (dom.console.debug("zoomingTransform", _)),
      /**
       * @see [[Phase]]
       * @todo Formalize this quick-and-dirty transition logic.
       */
      $states.combineWith($focusedState).withCurrentValueOf(phaseVar.signal).map {
        case (Nil, _, _) => Loading
        case (_, _, Loading) => Revealing
        case (_, _, Revealing) => Presenting
        case (_, _, phase) => phase
      } --> phaseVar.writer
    )
  }

}
