package ahlers.presence.web.client.resume

import cats.syntax.option._
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.signal.{ Signal, Var }
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveSvgElement
import d3v4.d3
import d3v4.d3.{ Transition, ZoomBehavior, ZoomEvent }
import d3v4.d3hierarchy.{ Hierarchy, Pack, Packed }
import d3v4.d3zoom.Transform
import io.scalaland.chimney.dsl.TransformerOps
import org.scalajs.dom
import org.scalajs.dom.console
import org.scalajs.dom.svg.{ G, SVG }

import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichIterableOnce

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceGridView {

  def render(): ReactiveSvgElement[SVG] = {
    import svg._
    svg(
      className := "experience-grid-view",
      className := "flex-fill bg-dark",
      gridZoomEventBinder,
      focusedNodeZoomBinder,
      onClick.mapToValue(none) --> focusedNodeIdVar.writer,
      g(
        transform <-- gridZoomEventBus.events.map(_.transform.toString()),
        children <-- $nodeRenders)
    )
  }

  val gridZoomBehavior: ZoomBehavior[dom.EventTarget] = d3.zoom()
  val gridZoomEventBus: EventBus[ZoomEvent] = new EventBus()

  /**
   * When mounted, monitors window resize events to keep [[gridZoomBehavior]] centered, and also send applicable [[ZoomEvent]] values to [[gridZoomEventBus]].
   *
   * @todo Learn if there's a more elegant, idiomatic technique to this.
   */
  val gridZoomEventBinder: Modifier[ReactiveSvgElement[SVG]] = {
    val mount: MountContext[ReactiveSvgElement[SVG]] => Unit = { context =>
      import context.{ owner, thisNode }
      import thisNode.ref.{ clientHeight, clientWidth }

      val $clientWidth =
        windowEvents
          .onResize.mapTo(clientWidth)
          .toSignal(clientWidth)

      val $clientHeight =
        windowEvents
          .onResize.mapTo(clientHeight)
          .toSignal(clientHeight)

      val selection = d3.select(thisNode.ref)

      /** @todo Overload [[d3v4.d3zoom.ZoomBehavior.on]] with [[https://github.com/d3/d3-selection#selection_on listener function type taking documented event, datum, and target]]. */
      val onZoomEvent = () =>
        gridZoomEventBus
          .writer
          .onNext(d3.event)

      gridZoomBehavior.on("zoom", onZoomEvent)(selection)

      /** @todo Figure out why the transform binding never receives the first event without this. */
      gridZoomEventBus
        .events
        .foreach(console.debug("Zoom event.", _))

      val $transform =
        for {
          clientWidth <- $clientWidth
          clientHeight <- $clientHeight
          transform =
            d3.zoomIdentity
              .translate(
                clientWidth / 2,
                clientHeight / 2)
        } yield transform

      $transform
        .foreach(gridZoomBehavior
          .transform(selection, _))
    }

    val unmount: ReactiveSvgElement[SVG] => Unit = { thisNode =>
      val selection = d3.select(thisNode.ref)
      gridZoomBehavior.on("zoom", null)(selection)
    }

    onMountUnmountCallback(mount, unmount)
  }

  val focusedNodeIdVar: Var[Option[ExperienceId]] = Var(none)
  val $focusedNodeId: StrictSignal[Option[ExperienceId]] = focusedNodeIdVar.signal

  val $focusedNodeState: Signal[Option[ExperienceNodeState]] =
    $focusedNodeId
      .flatMap {
        case None => Val(None)
        case Some(focusedId) => $nodeStates.map(_.find(_.id == focusedId))
      }

  val focusedNodeZoomBinder: Modifier[ReactiveSvgElement[SVG]] =
    inContext { thisNode: ReactiveSvgElement[SVG] =>
      import thisNode.ref.{ clientHeight, clientWidth }

      val transition = //: Transition[dom.EventTarget] =
        d3.select(thisNode.ref)
      //.transition()
      //.duration(1500d)

      val $transform: Signal[Transform] =
        $focusedNodeState
          .map {

            case None =>
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)

            case Some(nodeState) =>
              import nodeState.{ x, y }
              d3.zoomIdentity
                .translate(
                  clientWidth / 2,
                  clientHeight / 2)
                .scale(5)
                .translate(-x, -y)

          }

      $transform --> (gridZoomBehavior.transform(transition, _))
    }

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

  val $nodeRenders: Signal[Seq[ReactiveSvgElement[G]]] =
    nodeStatesVar
      .signal
      .split(_.id)(ExperienceNodeView
        .render(_, _, _, focusedNodeIdVar.writer))

}
