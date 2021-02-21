package ahlers.presence.web.client.resume

import ahlers.presence.experiences.{ Experience, ExperienceKey }
import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.{ FocusedResumePage, UnfocusedResumePage }
import cats.syntax.apply._
import cats.syntax.option._
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.eventstream.PeriodicEventStream
import com.raquo.airstream.signal.Signal
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import d3.laminar.syntax.zoom._
import d3v4.d3
import d3v4.d3.{ Transform, ZoomBehavior }
import d3v4.d3hierarchy.{ Hierarchy, Pack, Packed }
import org.scalajs.dom
import org.scalajs.dom.svg.SVG

import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichIterableOnce
import scala.scalajs.js.|

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperiencesGridView {

  val zoomBehavior: ZoomBehavior[dom.EventTarget] =
    d3.zoom()
      .scaleExtent(js.Array(0.5d, 5d))

  val zoomTransformBus: EventBus[Transform] = new EventBus()

  def briefStates(experiences: Seq[Experience]): Seq[ExperienceBriefState] = {
    val packed: Pack[ExperienceBriefState] =
      d3.pack()
        .radius(_.data.r * 1.2d)

    val hierarchy: Hierarchy[ExperienceBriefState] with Packed = {
      val root: ExperienceBriefState = ExperienceBriefState(ExperienceBriefIndex(-1), ExperienceBriefState.Mode.Root, 0, 0, 0)
      val children: Seq[ExperienceBriefState] =
        (experiences.map(_.some) ++ Seq.fill(500)(none))
          .zipWithIndex
          .map {
            case (None, index) =>
              ExperienceBriefState(ExperienceBriefIndex(index), ExperienceBriefState.Mode.Blank, 0, 0, 18d + Math.pow(index.toInt, 2) / 750)
            case (Some(experience), index) =>
              ExperienceBriefState(ExperienceBriefIndex(index), ExperienceBriefState.Mode.Content(experience), 0, 0, 20d)
          }

      packed(d3.hierarchy(
        root,
        {
          case x if root == x => children.toJSArray
          case _ => js.Array()
        }))
    }

    hierarchy.children.orNull
      .toSeq
      .map { hierarchy =>
        hierarchy.data.copy(
          cx = hierarchy.x.getOrElse(???),
          cy = hierarchy.y.getOrElse(???))
      }
  }

//def onWindowResizeZoom($focusedBriefState: Signal[Option[ExperienceBriefState]]): Modifier[ReactiveSvgElement[SVG]] =
//  inContext { thisNode =>
//    import thisNode.ref.{ clientHeight, clientWidth }
//
//    windowEvents
//      .onResize
//      .withCurrentValueOf($focusedBriefState) --> {
//
//      case (_, None) =>
//        //val briefState = defaultBriefState
//        //import briefState.{ cx, cy }
//
//        zoomBehavior
//          .transform(
//            d3.select(thisNode.ref),
//            d3.zoomIdentity
//              .translate(
//                clientWidth / 2,
//                clientHeight / 2))
//      //.translate(-cx, -cy))
//
//      case (_, Some(focusedBriefState)) =>
//        zoomBehavior
//          .transform(
//            d3.select(thisNode.ref),
//            d3.zoomIdentity
//              .translate(
//                clientWidth / 2,
//                clientHeight / 2)
//              .scale(5)
//              .translate(
//                -focusedBriefState.cx,
//                -focusedBriefState.cy)
//          )
//
//    }
//  }

  val onClickExitFocus =
    onClick
      .stopPropagation
      .mapToValue(none)

  def render(
    $experiences: Signal[Option[Seq[Experience]]],
    $focusedExperienceKey: Signal[Option[ExperienceKey]],
    focusedExperienceObserver: Observer[Option[ExperienceKey]]
  ): ReactiveSvgElement[SVG] = {
    import svg._

    val $briefStates: Signal[Seq[ExperienceBriefState]] =
      $experiences
        .map(_.getOrElse(Nil))
        .map(briefStates(_))

    val glancedExperienceKeysVar: Var[Set[ExperienceKey]] = Var(Set.empty)

    //val $focusedBriefState: Signal[Option[ExperienceBriefState.Brief]] =
    //  $focusedExperienceKeyId
    //    .map {
    //      case None => none
    //      case Some(id) =>
    //        briefStates.collectFirst {
    //          case brief: ExperienceBriefState.Brief if id == brief.id => brief
    //        }
    //    }

//val blankRenders =
//  briefStates
//    .collect { case briefState: ExperienceBriefState.Blank =>
//      ExperienceBriefBlankView
//        .render(briefState)
//    }

//val idleRenders =
//  briefStates
//    .collect { case briefState: ExperienceBriefState.Brief =>
//      val onMouseEnterGlanced =
//        onMouseEnter --> (_ => glancedBriefStatesVar.update(_ + briefState))
//
//      val onMouseLeaveGlanced =
//        onMouseLeave --> (_ => glancedBriefStatesVar.update(_ - briefState))
//
//      val onClickEnterFocus =
//        onClick
//          .stopPropagation
//          .mapToValue(FocusedResumePage(briefState.id)) --> (UiState.router.pushState(_))
//
//      ExperienceBriefIdleView
//        .render(
//          briefState,
//          onMouseEnterGlanced,
//          onMouseLeaveGlanced,
//          onClickEnterFocus)
//    }
//
//val focusRenders =
//  briefStates
//    .collect { case briefState: ExperienceBriefState.Brief =>
//      val $isFocused =
//        $focusedBriefState
//          .map(_.contains(briefState))
//
//      ExperienceBriefFocusView
//        .render(
//          briefState,
//          className.toggle("focused") <-- $isFocused)
//    }
//
//val glanceRenders =
//  briefStates
//    .collect { case briefState: ExperienceBriefState.Brief =>
//      val $isGlanced =
//        $glancedBriefStates
//          .combineWith($focusedBriefState)
//          .map { case (x, y) => x.diff(y.toSet) }
//          .map(_.contains(briefState))
//
//      ExperienceBriefGlanceView
//        .render(
//          briefState,
//          className.toggle("glanced") <-- $isGlanced)
//    }

    /**
     * View's lifecycle.
     * @todo Clean up this initial attempt.
     */
    sealed abstract class Phase(val isLoading: Boolean = false, val isWaiting: Boolean = false, val isRevealing: Boolean = false, val isPresenting: Boolean = false)
    object Phase {
      case object Loading extends Phase(isLoading = true)
      case object Waiting extends Phase(isWaiting = true)
      case object Revealing extends Phase(isRevealing = true)
      case object Presenting extends Phase(isPresenting = true)
    }

    val $phase = {
      import Phase._
      $experiences
        .flatMap {
          case None => Val(Loading)
          case Some(_) =>
            new PeriodicEventStream[Phase](
              initial = Waiting,
              next = {
                case Loading => Some((Waiting, 0))
                case Waiting => Some((Revealing, 100))
                case Revealing => Some((Presenting, 5000))
                case Presenting => none
              },
              emitInitial = false,
              resetOnStop = false)
              .toSignal(Waiting)
        }
    }

    /** Indicates if the viewer is hovering over any of the experiences, debounced to avoid rapid transitions between glancing and not. */
    val $isGlancing: Signal[Boolean] =
      glancedExperienceKeysVar.signal.map(_.nonEmpty)
        .changes
        .debounce(250)
        .toSignal(false)

    val $isFocusing: Signal[Boolean] =
      $focusedExperienceKey.map(_.nonEmpty)

    svg(
      className("experience-grid-view", "w-100", "h-100", "bg-dark"),
      zoomBehavior --> zoomTransformBus.writer.contramap(_.transform),
      g(
        className.toggle("loading") <-- $phase.map(_.isLoading),
        className.toggle("waiting") <-- $phase.map(_.isWaiting),
        className.toggle("revealing") <-- $phase.map(_.isRevealing),
        className.toggle("presenting") <-- $phase.map(_.isPresenting),
        className.toggle("glancing") <-- $isGlancing,
        className.toggle("focusing") <-- $isFocusing,
        transform <-- zoomTransformBus.events.map(_.toString()),
        g(children <--
          $briefStates
            .split(_.index)(ExperienceBriefView.render(_, _, _, focusedExperienceObserver, glancedExperienceKeysVar))),
        g(children <--
          $briefStates
            .map(_.filter(_.mode.isContent))
            .split(_.index)(ExperienceBriefGlanceView.render(_, _, _, glancedExperienceKeysVar.signal)))
      ),
      inContext { thisNode =>
        import thisNode.ref.{ clientHeight, clientWidth }
        import Phase._

        $phase
          .combineWith($briefStates)
          .combineWith($focusedExperienceKey)
          .map {
            case ((phase, states), Some(focusedExperienceKey)) =>
              (phase, states.headOption, states.find(_.key.contains(focusedExperienceKey)))
            case ((phase, states), None) =>
              (phase, states.headOption, none)
          } --> {

          case (Revealing, Some(state), None) =>
            zoomBehavior
              .transform(
                d3.select(thisNode.ref),
                d3.zoomIdentity
                  .translate(
                    clientWidth / 2,
                    clientHeight / 2)
                  .scale(0.5d)
                  .translate(
                    -state.cx,
                    -state.cy))

            zoomBehavior
              .transform(
                d3.select(thisNode.ref)
                  .transition()
                  .duration(3000d),
                d3.zoomIdentity
                  .translate(
                    clientWidth / 2,
                    clientHeight / 2)
                  .translate(
                    -state.cx,
                    -state.cy))

          case (Revealing, _, Some(state)) =>
            zoomBehavior
              .transform(
                d3.select(thisNode.ref),
                d3.zoomIdentity
                  .translate(
                    clientWidth / 2,
                    clientHeight / 2)
                  .scale(0.5d)
                  .translate(
                    -state.cx,
                    -state.cy))

            zoomBehavior
              .transform(
                d3.select(thisNode.ref)
                  .transition()
                  .duration(3000d),
                d3.zoomIdentity
                  .translate(
                    clientWidth / 2,
                    clientHeight / 2)
                  .scale(5d)
                  .translate(
                    -state.cx,
                    -state.cy))

          case (Presenting, Some(state), None) =>
            zoomBehavior
              .transform(
                d3.select(thisNode.ref)
                  .transition()
                  .duration(1000d),
                d3.zoomIdentity
                  .translate(
                    clientWidth / 2,
                    clientHeight / 2)
                  .translate(
                    -state.cx,
                    -state.cy))

          case (Presenting, _, Some(state)) =>
            zoomBehavior
              .transform(
                d3.select(thisNode.ref)
                  .transition()
                  .duration(1000d),
                d3.zoomIdentity
                  .translate(
                    clientWidth / 2,
                    clientHeight / 2)
                  .scale(5d)
                  .translate(
                    -state.cx,
                    -state.cy))

          case x =>
            println(s"Unhandled: $x")

        }
      },
      onClickExitFocus --> focusedExperienceObserver
    )
  }

}
