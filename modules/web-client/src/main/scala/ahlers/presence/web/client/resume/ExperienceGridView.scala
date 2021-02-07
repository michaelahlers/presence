package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.{ FocusedResumePage, UnfocusedResumePage }
import cats.laminar.instances.signal._
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
  val zoomTransform: EventBus[Transform] = new EventBus()

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
  val $nodeStates: StrictSignal[Seq[ExperienceNodeState]] = nodeStatesVar.signal

  val focusedIdVar: Var[Option[ExperienceId]] = Var(none)
  val $focusedId: Signal[Option[ExperienceId]] = focusedIdVar.signal

  val $focusedNode: Signal[Option[ExperienceNodeState]] =
    ($focusedId, $nodeStates)
      .mapN {
        case (None, _) => None
        case (Some(id), nodeStates) => nodeStates.find(_.id == id)
      }

  def focusedNodeNow(): Option[ExperienceNodeState] =
    (focusedIdVar.now(), nodeStatesVar.now()) match {
      case (None, _) => None
      case (Some(id), nodeStates) => nodeStates.find(_.id == id)
    }

  val handleWindowLoad: Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      windowEvents.onLoad --> { _ =>
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
      }
    }

  val handleWindowResize: Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      windowEvents.onResize.mapTo(focusedNodeNow()) --> {

        case None =>
          zoomBehavior
            .transform(
              d3.select(thisNode.ref),
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2))

        case Some(nodeState) =>
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

  val handleFocusedNode: Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode =>
      import thisNode.ref.{ clientHeight, clientWidth }

      $focusedNode --> {

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
      zoomBehavior --> zoomTransform.writer.contramap(_.transform),
      handleWindowLoad,
      handleWindowResize,
      handleFocusedNode,
      $focusedExperienceId --> focusedIdVar.writer,
      $focusedExperienceId --> (dom.console.debug("Focused experience id.", _)),
      handleClick,
      g(
        transform <-- zoomTransform.events.map(_.toString()),
        children <-- $nodeRenders)
    )
  }

}
