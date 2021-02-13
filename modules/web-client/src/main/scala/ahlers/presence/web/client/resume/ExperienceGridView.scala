package ahlers.presence.web.client.resume

import ahlers.presence.web.client.{ Asset, UiState }
import ahlers.presence.web.client.UiState.{ FocusedResumePage, UnfocusedResumePage }
import cats.syntax.apply._
import cats.syntax.option._
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.signal.{ Signal, Var }
import com.raquo.domtypes.generic.Modifier
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

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceGridView {

  val zoomBehavior: ZoomBehavior[dom.EventTarget] = d3.zoom()
  val zoomTransformBus: EventBus[Transform] = new EventBus()

  //val nodeStatesVar = {
  val nodeStates = {
    import ExperienceBrief.{ Blank, Employment, Root, Skill }

    val packed: Pack[ExperienceBrief] =
      d3.pack()
        .padding(15)
        .radius(_ => 20) // + Random.nextInt(20))

    val hierarchy: Hierarchy[ExperienceBrief] with Packed =
      packed(d3.hierarchy(
        Root,
        {
          case Root => (experiences.descriptions ++ Seq.fill(2000)(Blank)).toJSArray
          case _ => js.Array()
        }))

    //Val(hierarchy.children.orNull
    hierarchy.children.orNull
      .toSeq
      .zipWithIndex
      .map {
        case (hierarchy, index) =>
          hierarchy.data match {

            case Root => ???

            case Blank =>
              ExperienceNodeState(
                ExperienceNodeIndex(index),
                none,
                none,
                hierarchy.x.getOrElse(???),
                hierarchy.y.getOrElse(???),
                20d)

            case employment: Employment =>
              employment
                .into[ExperienceNodeState]
                .withFieldConst(_.index, ExperienceNodeIndex(index))
                .withFieldConst(_.cx, hierarchy.x.getOrElse(???))
                .withFieldConst(_.cy, hierarchy.y.getOrElse(???))
                .withFieldConst(_.radius, 20d)
                .transform

            case skill: Skill =>
              skill
                .into[ExperienceNodeState]
                .withFieldConst(_.index, ExperienceNodeIndex(index))
                .withFieldConst(_.cx, hierarchy.x.getOrElse(???))
                .withFieldConst(_.cy, hierarchy.y.getOrElse(???))
                .withFieldConst(_.radius, 20d)
                .transform

          }
        //})
      }
  }

  //val $nodeStates: Signal[Seq[ExperienceNodeState]] = nodeStatesVar.signal

  def handleWindowLoad($focusedNodeState: Signal[Option[ExperienceNodeState]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      windowEvents
        .onLoad
        .withCurrentValueOf($focusedNodeState) --> {

        case (_, None) =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(0.5d))

          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(3000d)
                .delay(2000d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2))

        case (_, Some(nodeState)) =>
          import nodeState.{ cx, cy }
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(0.5d))

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
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2))

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
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(1000d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2))

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

    //val $nodeRenders: Signal[Seq[ReactiveSvgElement[G]]] =
    //  $nodeStates
    //    .split(_.index)(ExperienceNodeView
    //      .render(_, _, _, $focusedExperienceId))
    val nodeRenders: Seq[ReactiveSvgElement[G]] =
      nodeStates
        .map(ExperienceNodeView.render(_, $focusedExperienceId))

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
        nodeRenders)
    )
  }

}
