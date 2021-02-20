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

  val nodeStates: Seq[ExperienceNodeState] = {
    import ExperienceBrief.{ Employment, Skill }

    val packed: Pack[ExperienceNodeState] =
      d3.pack()
        .radius(_.data.r * 1.2d)

    val hierarchy: Hierarchy[ExperienceNodeState] with Packed = {
      val root: ExperienceNodeState = ExperienceNodeState.Root
      val children: Seq[ExperienceNodeState] =
        (experiences.descriptions.map(_.some) ++ Seq.fill(500)(none))
          .zipWithIndex
          .map {
            case (None, index) =>
              ExperienceNodeState.Blank(ExperienceNodeIndex(index), 0, 0, 18d + Math.pow(index.toInt, 2) / 750)
            case (Some(skill: Skill), index) =>
              ExperienceNodeState.Brief(ExperienceNodeIndex(index), 0, 0, 20d, skill.id, skill.name.toText, skill.logo)
            case (Some(employment: Employment), index) =>
              ExperienceNodeState.Brief(ExperienceNodeIndex(index), 0, 0, 20d, employment.id, employment.company.shortName.getOrElse(employment.company.name), employment.logo)
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
          case ExperienceNodeState.Root => ???
          case blank: ExperienceNodeState.Blank =>
            blank.copy(
              cx = hierarchy.x.getOrElse(???),
              cy = hierarchy.y.getOrElse(???))
          case brief: ExperienceNodeState.Brief =>
            brief.copy(
              cx = hierarchy.x.getOrElse(???),
              cy = hierarchy.y.getOrElse(???))
        }
      }
  }

  val defaultNodeState: ExperienceNodeState = nodeStates.head

  /** Initial zoom effect, starting wide and narrowing gradually. */
  def onMountZoom($focusedNodeState: Signal[Option[ExperienceNodeState]]): Modifier[ReactiveSvgElement[SVG]] =
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
            .translate(-defaultNodeState.cx, -defaultNodeState.cy))

      $focusedNodeState.observe.now() match {

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
                .translate(-defaultNodeState.cx, -defaultNodeState.cy)
            )

        case Some(focusedNodeState) =>
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
                  -focusedNodeState.cx,
                  -focusedNodeState.cy)
            )

      }
    }

  def onWindowResizeZoom($focusedNodeState: Signal[Option[ExperienceNodeState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      windowEvents
        .onResize
        .withCurrentValueOf($focusedNodeState) --> {

        case (_, None) =>
          val nodeState = defaultNodeState
          import nodeState.{ cx, cy }

          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .translate(-cx, -cy))

        case (_, Some(focusedNodeState)) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(5)
                .translate(
                  -focusedNodeState.cx,
                  -focusedNodeState.cy)
            )

      }
    }

  def onFocusedNodeZoom($focusedNodeState: Signal[Option[ExperienceNodeState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      $focusedNodeState.changes --> {

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
                .translate(-defaultNodeState.cx, -defaultNodeState.cy)
            )

        case Some(focusedNodeState) =>
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
                  -focusedNodeState.cx,
                  -focusedNodeState.cy)
            )

      }
    }

  val onClickExitFocus: Modifier[ReactiveSvgElement[SVG]] =
    onClick
      .stopPropagation
      .mapToValue(UnfocusedResumePage) --> (UiState.router.pushState(_))

  def render($focusedExperienceId: Signal[Option[ExperienceId]]): ReactiveSvgElement[SVG] = {
    import svg._

    val glancedNodeStatesVar: Var[Set[ExperienceNodeState]] = Var(Set.empty)

    val $glancedNodeStates: Signal[Set[ExperienceNodeState]] =
      glancedNodeStatesVar.signal

    val $focusedNodeState: Signal[Option[ExperienceNodeState.Brief]] =
      $focusedExperienceId
        .map {
          case None => none
          case Some(id) =>
            nodeStates.collectFirst {
              case brief: ExperienceNodeState.Brief if id == brief.id => brief
            }
        }

    val blankRenders =
      nodeStates
        .collect { case nodeState: ExperienceNodeState.Blank =>
          ExperienceBlankView
            .render(nodeState)
        }

    val idleRenders =
      nodeStates
        .collect { case nodeState: ExperienceNodeState.Brief =>
          val onMouseEnterGlanced =
            onMouseEnter --> (_ => glancedNodeStatesVar.update(_ + nodeState))

          val onMouseLeaveGlanced =
            onMouseLeave --> (_ => glancedNodeStatesVar.update(_ - nodeState))

          val onClickEnterFocus =
            onClick
              .stopPropagation
              .mapToValue(FocusedResumePage(nodeState.id)) --> (UiState.router.pushState(_))

          ExperienceIdleView
            .render(
              nodeState,
              onMouseEnterGlanced,
              onMouseLeaveGlanced,
              onClickEnterFocus)
        }

    val focusRenders =
      nodeStates
        .collect { case nodeState: ExperienceNodeState.Brief =>
          val $isFocused =
            $focusedNodeState
              .map(_.contains(nodeState))

          ExperienceFocusView
            .render(
              nodeState,
              className.toggle("focused") <-- $isFocused)
        }

    val glanceRenders =
      nodeStates
        .collect { case nodeState: ExperienceNodeState.Brief =>
          val $isGlanced =
            $glancedNodeStates
              .combineWith($focusedNodeState)
              .map { case (x, y) => x.diff(y.toSet) }
              .map(_.contains(nodeState))

          ExperienceGlanceView
            .render(
              nodeState,
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
      $glancedNodeStates.map(_.nonEmpty)
        .changes
        .debounce(250)
        .toSignal(false)

    val $isFocusing: Signal[Boolean] =
      $focusedNodeState.map(_.nonEmpty)

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
      onMountZoom($focusedNodeState),
      onWindowResizeZoom($focusedNodeState),
      onFocusedNodeZoom($focusedNodeState),
      onClickExitFocus
    )
  }

}
