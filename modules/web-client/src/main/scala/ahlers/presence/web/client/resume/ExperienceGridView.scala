package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.UnfocusedResumePage
import cats.syntax.apply._
import cats.syntax.option._
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.eventstream.PeriodicEventStream
import com.raquo.airstream.signal.Signal
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.CollectionCommand
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import d3.laminar.syntax.zoom._
import d3v4.d3
import d3v4.d3.{ Transform, ZoomBehavior }
import d3v4.d3hierarchy.{ Hierarchy, Pack, Packed }
import io.scalaland.chimney.dsl.TransformerOps
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
              none,
              none,
              hierarchy.x.getOrElse(???),
              hierarchy.y.getOrElse(???),
              radius)

          case employment: Employment =>
            employment
              .into[ExperienceNodeState]
              .withFieldConst(_.index, ExperienceNodeIndex(index))
              .withFieldConst(_.cx, hierarchy.x.getOrElse(???))
              .withFieldConst(_.cy, hierarchy.y.getOrElse(???))
              .withFieldConst(_.radius, employment.radius)
              .transform

          case skill: Skill =>
            skill
              .into[ExperienceNodeState]
              .withFieldConst(_.index, ExperienceNodeIndex(index))
              .withFieldConst(_.cx, hierarchy.x.getOrElse(???))
              .withFieldConst(_.cy, hierarchy.y.getOrElse(???))
              .withFieldConst(_.radius, skill.radius)
              .transform

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
                .duration(3000d)
                .delay(2000d),
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

    /** Gradually emit rendered experience nodes, which get appended to the illustration. */
    val nodeRenderStream =
      new PeriodicEventStream[Int](
        initial = 0,
        next =
          Some(_)
            .map(_ + 1)
            .filter(_ < nodeStates.size)
            .map((_, 10)),
        emitInitial = true,
        resetOnStop = false)
        .map(nodeStates(_))
        .map(ExperienceNodeView.render(_))

    val $focusedNodeState: Signal[Option[ExperienceNodeState]] =
      $focusedExperienceId
        .map {
          case None => none
          case Some(id) => nodeStates.find(_.id.contains(id))
        }

    svg(
      className := "experience-grid-view",
      className := "flex-fill bg-dark",
      zoomBehavior --> zoomTransformBus.writer.contramap(_.transform),
      g(
        transform <-- zoomTransformBus.events.map(_.toString()),
        children.command <-- nodeRenderStream.map(CollectionCommand.Append(_))),
      onMountZoom($focusedNodeState),
      onWindowResizeZoom($focusedNodeState),
      onFocusedNodeZoom($focusedNodeState),
      onClickExitFocus
    )
  }

}
