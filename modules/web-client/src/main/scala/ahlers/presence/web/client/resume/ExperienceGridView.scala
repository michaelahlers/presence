package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.{ FocusedResumePage, UnfocusedResumePage }
import cats.laminar.instances.all._
import cats.syntax.apply._
import cats.syntax.option._
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.signal.{ Signal, Var }
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import cats.laminar.instances.all._
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

  val nodeStatesVar = {
    import ExperienceBrief.{ Blank, Employment, Skill }

    val pack: Pack[ExperienceBrief] =
      d3.pack()
        .padding(10)
        .radius(_ => 20) // + Random.nextInt(20))

    val hierarchy: Hierarchy[ExperienceBrief] with Packed = {
      val root = ExperienceBrief.Blank(ExperienceId("root"))
      pack.apply(d3.hierarchy(
        root,
        {
          case x if x.id == root.id => experiences.descriptions.toJSArray
          case _ => js.Array()
        }))
    }

    Var(hierarchy.children.orNull
      .toSeq
      .map { hierarchy =>
        hierarchy.data match {
          case _: Blank => ???
          case employment: Employment =>
            employment
              .into[ExperienceNodeState]
              .withFieldConst(_.x, hierarchy.x.getOrElse(???))
              .withFieldConst(_.y, hierarchy.y.getOrElse(???))
              .transform
          case skill: Skill =>
            skill
              .into[ExperienceNodeState]
              .withFieldConst(_.x, hierarchy.x.getOrElse(???))
              .withFieldConst(_.y, hierarchy.y.getOrElse(???))
              .transform
        }
      })
  }

  val $nodeStates: Signal[Seq[ExperienceNodeState]] = nodeStatesVar.signal

  def handleWindowLoad($focusedExperienceId: Signal[Option[ExperienceId]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      val $focusedNodeState: Signal[Option[ExperienceNodeState]] =
        ($focusedExperienceId, $nodeStates)
          .mapN {
            case (None, _) => none
            case (Some(id), nodeStates) => nodeStates.find(_.id == id)
          }

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
                .duration(3000d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2))

        case (_, Some(nodeState)) =>
          import nodeState.{ x, y }
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
                .translate(-x, -y))

      }
    }

  def handleWindowResize($focusedExperienceId: Signal[Option[ExperienceId]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      val $focusedNodeState: Signal[Option[ExperienceNodeState]] =
        ($focusedExperienceId, $nodeStates)
          .mapN {
            case (None, _) => none
            case (Some(id), nodeStates) => nodeStates.find(_.id == id)
          }

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
          import nodeState.{ x, y }
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(5)
                .translate(-x, -y))

      }
    }

  def handleFocusedNode($focusedExperienceId: Signal[Option[ExperienceId]]): Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      val $focusedNodeState: Signal[Option[ExperienceNodeState]] =
        ($focusedExperienceId, $nodeStates)
          .mapN {
            case (None, _) => none
            case (Some(id), nodeStates) => nodeStates.find(_.id == id)
          }

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
          import nodeState.{ x, y }
          zoomBehavior
            .transform(
              d3.select(thisNode.ref)
                .transition()
                .duration(1500d),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(5)
                .translate(-x, -y))

      }
    }

  val handleClick: Modifier[ReactiveSvgElement[SVG]] =
    onClick
      .stopPropagation
      .mapToValue(UnfocusedResumePage) --> (UiState.router.pushState(_))

  def render($focusedExperienceId: Signal[Option[ExperienceId]]): ReactiveSvgElement[SVG] = {
    import svg._

    val $nodeRenders: Signal[Seq[ReactiveSvgElement[G]]] =
      $nodeStates
        .split(_.id)(ExperienceNodeView
          .render(_, _, _, $focusedExperienceId))

    svg(
      className := "experience-grid-view",
      className := "flex-fill bg-dark",
      zoomBehavior --> zoomTransformBus.writer.contramap(_.transform),
      handleWindowLoad($focusedExperienceId),
      handleWindowResize($focusedExperienceId),
      handleFocusedNode($focusedExperienceId),
      $focusedExperienceId --> (dom.console.debug("Focused experience id.", _)),
      handleClick,
      g(
        transform <-- zoomTransformBus.events.map(_.toString()),
        children <-- $nodeRenders)
    )
  }

}
