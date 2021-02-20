package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.UnfocusedResumePage
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

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceGridView {

  val zoomBehavior: ZoomBehavior[dom.EventTarget] =
    d3.zoom()
      .scaleExtent(js.Array(0.5d, 5d))

  val zoomTransformBus: EventBus[Transform] = new EventBus()

  val nodeStates: Seq[ExperienceNodeState] = {
    import ExperienceBrief.{ Blank, Employment, Skill }

    val packed: Pack[ExperienceBrief] =
      d3.pack()
        .radius(_.data match {
          case blank: Blank => blank.radius * 1.2d
          case employment: Employment => employment.radius * 1.2d
          case skill: Skill => skill.radius * 1.2d
        })

    val hierarchy: Hierarchy[ExperienceBrief] with Packed = {
      val root = Blank(0)
      val children = experiences.descriptions ++ Seq.tabulate(500)(index => Blank(18d + Math.pow(index, 2) / 750))
      packed(d3.hierarchy(
        root,
        {
          case x if root == x => children.toJSArray
          case _ => js.Array()
        }))
    }

    hierarchy.children.orNull
      .toSeq
      .zipWithIndex
      .map { case (hierarchy, index) =>
        hierarchy.data match {

          case Blank(radius) =>
            ExperienceNodeState(
              ExperienceNodeIndex(index),
              "blank",
              none,
              none,
              none,
              hierarchy.x.getOrElse(???),
              hierarchy.y.getOrElse(???),
              radius)

          case employment: Employment =>
            ExperienceNodeState(
              ExperienceNodeIndex(index),
              "employment",
              employment.id.some,
              employment.company.shortName.getOrElse(employment.company.name).some,
              employment.logo.some,
              hierarchy.x.getOrElse(???),
              hierarchy.y.getOrElse(???),
              employment.radius
            )

          case skill: Skill =>
            ExperienceNodeState(
              ExperienceNodeIndex(index),
              "skill",
              skill.id.some,
              skill.name.toText.some,
              skill.logo.some,
              hierarchy.x.getOrElse(???),
              hierarchy.y.getOrElse(???),
              skill.radius
            )

        }
      }
  }

  val defaultNodeState: ExperienceNodeState = nodeStates.head

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
                .translate(-focusedNodeState.cx, -focusedNodeState.cy)
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
                .translate(-focusedNodeState.cx, -focusedNodeState.cy))

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
                .translate(-focusedNodeState.cx, -focusedNodeState.cy)
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

    val $focusedNodeState: Signal[Option[ExperienceNodeState]] =
      $focusedExperienceId
        .map {
          case None => none
          case Some(id) => nodeStates.find(_.id.contains(id))
        }

    val nodeRenders =
      nodeStates
        .map { nodeState =>
          val onMouseEnterGlanced =
            onMouseEnter --> (_ => glancedNodeStatesVar.update(_ + nodeState))

          val onMouseLeaveGlanced =
            onMouseLeave --> (_ => glancedNodeStatesVar.update(_ - nodeState))

          ExperienceIdleView
            .render(
              nodeState,
              //$glancedNodeStates,
              //$focusedNodeState,
              onMouseEnterGlanced,
              onMouseLeaveGlanced)
        }

    val focusRenders =
      nodeStates
        .filterNot(_.kind == "blank")
        .map { nodeState =>
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
        .filterNot(_.kind == "blank")
        .map { nodeState =>
          val $isGlanced =
            $glancedNodeStates
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
        nodeRenders,
        focusRenders,
        glanceRenders),
      onMountZoom($focusedNodeState),
      onWindowResizeZoom($focusedNodeState),
      onFocusedNodeZoom($focusedNodeState),
      onClickExitFocus
    )
  }

}
