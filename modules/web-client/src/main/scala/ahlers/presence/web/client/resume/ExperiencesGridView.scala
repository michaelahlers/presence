package ahlers.presence.web.client.resume

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

  val briefStates: Seq[ExperienceBriefState] = {
    import ExperienceDetail.{ Employment, Skill }

    val packed: Pack[ExperienceBriefState] =
      d3.pack()
        .radius(_.data.r * 1.2d)

    val hierarchy: Hierarchy[ExperienceBriefState] with Packed = {
      val root: ExperienceBriefState = ExperienceBriefState.Root
      val children: Seq[ExperienceBriefState] =
        (experiences.descriptions.map(_.some) ++ Seq.fill(500)(none))
          .zipWithIndex
          .map {
            case (None, index) =>
              ExperienceBriefState.Blank(ExperienceBriefIndex(index), 0, 0, 18d + Math.pow(index.toInt, 2) / 750)
            case (Some(skill: Skill), index) =>
              ExperienceBriefState.Brief(ExperienceBriefIndex(index), 0, 0, 20d, skill.id, skill.name.toText, skill.logo)
            case (Some(employment: Employment), index) =>
              ExperienceBriefState.Brief(ExperienceBriefIndex(index), 0, 0, 20d, employment.id, employment.company.shortName.getOrElse(employment.company.name), employment.logo)
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
        hierarchy.data match {
          case ExperienceBriefState.Root => ???
          case blank: ExperienceBriefState.Blank =>
            blank.copy(
              cx = hierarchy.x.getOrElse(???),
              cy = hierarchy.y.getOrElse(???))
          case brief: ExperienceBriefState.Brief =>
            brief.copy(
              cx = hierarchy.x.getOrElse(???),
              cy = hierarchy.y.getOrElse(???))
        }
      }
  }

  val defaultBriefState: ExperienceBriefState = briefStates.head

  /** Initial zoom effect, starting wide and narrowing gradually. */
  def onMountZoom($focusedBriefState: Signal[Option[ExperienceBriefState]]): Modifier[ReactiveSvgElement[SVG]] =
    onMountCallback { context =>
      import context.{ owner, thisNode }
      import thisNode.ref.{ clientHeight, clientWidth }

      zoomBehavior
        .transform(
          d3.select(thisNode.ref),
          d3.zoomIdentity
            .translate(
              clientWidth / 2,
              clientHeight / 2)
            .scale(0.5d)
            .translate(-defaultBriefState.cx, -defaultBriefState.cy))

      $focusedBriefState.observe.now() match {

        case None =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(3000d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .translate(-defaultBriefState.cx, -defaultBriefState.cy)
            )

        case Some(focusedBriefState) =>
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
                  -focusedBriefState.cx,
                  -focusedBriefState.cy)
            )

      }
    }

  def onWindowResizeZoom($focusedBriefState: Signal[Option[ExperienceBriefState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      windowEvents
        .onResize
        .withCurrentValueOf($focusedBriefState) --> {

        case (_, None) =>
          val briefState = defaultBriefState
          import briefState.{ cx, cy }

          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .translate(-cx, -cy))

        case (_, Some(focusedBriefState)) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(5)
                .translate(
                  -focusedBriefState.cx,
                  -focusedBriefState.cy)
            )

      }
    }

  def onFocusedBriefZoom($focusedBriefState: Signal[Option[ExperienceBriefState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      $focusedBriefState.changes --> {

        case None =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(1000d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .translate(-defaultBriefState.cx, -defaultBriefState.cy)
            )

        case Some(focusedBriefState) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(1000d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(5)
                .translate(
                  -focusedBriefState.cx,
                  -focusedBriefState.cy)
            )

      }
    }

  val onClickExitFocus: Modifier[ReactiveSvgElement[SVG]] =
    onClick
      .stopPropagation
      .mapToValue(UnfocusedResumePage) --> (UiState.router.pushState(_))

  def render($focusedExperienceId: Signal[Option[ExperienceId]]): ReactiveSvgElement[SVG] = {
    import svg._

    val glancedBriefStatesVar: Var[Set[ExperienceBriefState]] = Var(Set.empty)

    val $glancedBriefStates: Signal[Set[ExperienceBriefState]] =
      glancedBriefStatesVar.signal

    val $focusedBriefState: Signal[Option[ExperienceBriefState.Brief]] =
      $focusedExperienceId
        .map {
          case None => none
          case Some(id) =>
            briefStates.collectFirst {
              case brief: ExperienceBriefState.Brief if id == brief.id => brief
            }
        }

    val blankRenders =
      briefStates
        .collect { case briefState: ExperienceBriefState.Blank =>
          ExperienceBriefBlankView
            .render(briefState)
        }

    val idleRenders =
      briefStates
        .collect { case briefState: ExperienceBriefState.Brief =>
          val onMouseEnterGlanced =
            onMouseEnter --> (_ => glancedBriefStatesVar.update(_ + briefState))

          val onMouseLeaveGlanced =
            onMouseLeave --> (_ => glancedBriefStatesVar.update(_ - briefState))

          val onClickEnterFocus =
            onClick
              .stopPropagation
              .mapToValue(FocusedResumePage(briefState.id)) --> (UiState.router.pushState(_))

          ExperienceBriefIdleView
            .render(
              briefState,
              onMouseEnterGlanced,
              onMouseLeaveGlanced,
              onClickEnterFocus)
        }

    val focusRenders =
      briefStates
        .collect { case briefState: ExperienceBriefState.Brief =>
          val $isFocused =
            $focusedBriefState
              .map(_.contains(briefState))

          ExperienceBriefFocusView
            .render(
              briefState,
              className.toggle("focused") <-- $isFocused)
        }

    val glanceRenders =
      briefStates
        .collect { case briefState: ExperienceBriefState.Brief =>
          val $isGlanced =
            $glancedBriefStates
              .combineWith($focusedBriefState)
              .map { case (x, y) => x.diff(y.toSet) }
              .map(_.contains(briefState))

          ExperienceBriefGlanceView
            .render(
              briefState,
              className.toggle("glanced") <-- $isGlanced)
        }

    /**
     * View's lifecycle.
     * @todo Clean up this initial attempt.
     */
    sealed abstract class Phase(val isWaiting: Boolean = false, val isRevealing: Boolean = false, val isPresenting: Boolean = false)
    object Phase {
      case object Waiting extends Phase(isWaiting = true)
      case object Revealing extends Phase(isRevealing = true)
      case object Presenting extends Phase(isPresenting = true)
    }

    val $phase = {
      import Phase._
      new PeriodicEventStream[Phase](
        initial = Waiting,
        next = {
          case Waiting => Some((Revealing, 100))
          case Revealing => Some((Presenting, 5000))
          case Presenting => none
        },
        emitInitial = false,
        resetOnStop = false)
        .toSignal(Waiting)
    }

    /** Indicates if the viewer is hovering over any of the experiences, debounced to avoid rapid transitions between glancing and not. */
    val $isGlancing: Signal[Boolean] =
      $glancedBriefStates.map(_.nonEmpty)
        .changes
        .debounce(250)
        .toSignal(false)

    val $isFocusing: Signal[Boolean] =
      $focusedBriefState.map(_.nonEmpty)

    val $classNames: Signal[Map[String, Boolean]] =
      $phase
        .combineWith($isGlancing)
        .combineWith($isFocusing)
        .map { case ((phase, isGlancing), isFocusing) =>
          dom.console.debug(s"Foo: $phase; isGlancing: $isGlancing, isFocusing: $isFocusing")

          Map(
            "waiting" -> phase.isWaiting,
            "revealing" -> phase.isRevealing,
            "presenting" -> phase.isPresenting,
            "glancing" -> isGlancing,
            "focusing" -> isFocusing)
        }

    svg(
      className := "experience-grid-view",
      className := Seq("flex-fill", "bg-dark"),
      zoomBehavior --> zoomTransformBus.writer.contramap(_.transform),
      g(
        className <-- $classNames,
        transform <-- zoomTransformBus.events.map(_.toString()),
        blankRenders,
        idleRenders,
        focusRenders,
        glanceRenders),
      onMountZoom($focusedBriefState),
      onWindowResizeZoom($focusedBriefState),
      onFocusedBriefZoom($focusedBriefState),
      onClickExitFocus
    )
  }

}
