package ahlers.presence.web.client.resume

import ahlers.presence.web.client.{ Asset, UiState }
import ahlers.presence.web.client.UiState.{ FocusedResumePage, UnfocusedResumePage }
import cats.syntax.apply._
import cats.syntax.option._
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.eventstream.PeriodicEventStream
import com.raquo.airstream.signal.{ Signal, Var }
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
import org.scalajs.dom.svg.{ G, SVG }

import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichIterableOnce
import scala.util.Random

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceGridView {

  val zoomBehavior: ZoomBehavior[dom.EventTarget] = d3.zoom()
  val zoomTransformBus: EventBus[Transform] = new EventBus()

  //val nodeStatesVar = {
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
      val children = experiences.descriptions ++ Seq.tabulate(500)(index => Blank(10d + Math.pow(index - 90, 2) / 1000d))
      packed(d3.hierarchy(
        root,
        {
          case x if root == x => children.toJSArray
          case _ => js.Array()
        }))
    }

    //Val(hierarchy.children.orNull
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
      //})
      }
  }

  //val $nodeStates: Signal[Seq[ExperienceNodeState]] = nodeStatesVar.signal

  val nodeStateStream: EventStream[ExperienceNodeState] =
    new PeriodicEventStream[Int](
      initial = 0,
      next = Some(_).map(_ + 1).filter(_ < nodeStates.size).map((_, Random.nextInt(10))),
      emitInitial = true,
      resetOnStop = false)
      .map(nodeStates(_))

  def handleWindowLoad($focusedNodeState: Signal[Option[ExperienceNodeState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      windowEvents
        .onLoad
        .withCurrentValueOf($focusedNodeState) --> {

        case (_, None) =>
          val nodeState = nodeStates.head
          import nodeState.{ cx, cy }

          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(0.5d)
                .translate(-cx, -cy))

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
                .translate(-cx, -cy))

        case (_, Some(nodeState)) =>
          import nodeState.{ cx, cy }
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(0.5d)
                .translate(-cx, -cy))

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
                .translate(-cx, -cy))

      }
    }

  def handleWindowResize($focusedNodeState: Signal[Option[ExperienceNodeState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      windowEvents
        .onResize
        .withCurrentValueOf($focusedNodeState) --> {

        case (_, None) =>
          val nodeState = nodeStates.head
          import nodeState.{ cx, cy }

          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .translate(-cx, -cy))

        case (_, Some(nodeState)) =>
          import nodeState.{ cx, cy }
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(5)
                .translate(-cx, -cy))

      }
    }

  def handleFocusedNode($focusedNodeState: Signal[Option[ExperienceNodeState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      $focusedNodeState --> {

        case None =>
          val nodeState = nodeStates.head
          import nodeState.{ cx, cy }

          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(1000d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .translate(-cx, -cy))

        case Some(nodeState) =>
          import nodeState.{ cx, cy }
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
                .translate(-cx, -cy))

      }
    }

  val handleClick: Modifier[ReactiveSvgElement[SVG]] =
    onClick
      .stopPropagation
      .mapToValue(UnfocusedResumePage) --> (UiState.router.pushState(_))

  def render($focusedExperienceId: Signal[Option[ExperienceId]]): ReactiveSvgElement[SVG] = {
    import svg._

    val commandBus = new EventBus[ChildrenCommand]
    val commandStream = commandBus.events

//val $nodeRenders: Signal[Seq[ReactiveSvgElement[G]]] =
//  $nodeStates
//    .split(_.index)(ExperienceNodeView
//      .render(_, _, _, $focusedExperienceId))

    val $focusedNodeState: Signal[Option[ExperienceNodeState]] =
      $focusedExperienceId
        //.combineWith($nodeStates)
        .map {
          //case (None, _) => none
          case None => none
          //case (Some(id), nodeStates) => nodeStates.find(_.id.contains(id))
          case Some(id) => nodeStates.find(_.id.contains(id))
        }

    svg(
      className := "experience-grid-view",
      className := "flex-fill bg-dark",
      zoomBehavior --> zoomTransformBus.writer.contramap(_.transform),
      handleWindowLoad($focusedNodeState),
      handleWindowResize($focusedNodeState),
      handleFocusedNode($focusedNodeState),
      $focusedExperienceId --> (dom.console.debug("Focused experience id.", _)),
      handleClick,
      g(
        transform <-- zoomTransformBus.events.map(_.toString()),
        //children <-- $nodeRenders)
        nodeStateStream.map(ExperienceNodeView.render(_)).map(CollectionCommand.Append(_)) --> commandBus.writer,
        children.command <-- commandStream
      )
    )
  }

}
