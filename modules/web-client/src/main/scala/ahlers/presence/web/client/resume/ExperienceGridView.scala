package ahlers.presence.web.client.resume

import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.signal.{ Signal, Var }
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import com.raquo.laminar.keys.ReactiveEventProp
import com.raquo.laminar.nodes.ReactiveSvgElement
import d3.laminar.syntax.zoom._
import d3v4.d3
import d3v4.d3.{ Selection, ZoomBehavior, ZoomEvent }
import io.scalaland.chimney.dsl.TransformerOps
import org.scalajs.dom
import org.scalajs.dom.svg.{ G, SVG }

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
      zoomBehavior --> zoomEventBus.writer,
      zoomEventBinder,
      g(
        transform <-- zoomEventBus.events.map(_.transform.toString()),
        children <-- $nodeRenders)
    )
  }

  val zoomBehavior: ZoomBehavior[dom.EventTarget] = d3.zoom()
  val zoomEventBus: EventBus[ZoomEvent] = new EventBus()
  val zoomEventBinder: Modifier[ReactiveSvgElement[SVG]] =
    onMountCallback { context =>
      import context.{ owner, thisNode }

      val $clientWidth =
        windowEvents
          .onResize.mapTo(thisNode.ref.clientWidth)
          .toSignal(thisNode.ref.clientWidth)

      val $clientHeight =
        windowEvents
          .onResize.mapTo(thisNode.ref.clientHeight)
          .toSignal(thisNode.ref.clientHeight)

      (for {
        clientWidth <- $clientWidth
        clientHeight <- $clientHeight
        zoomIdentity =
          d3.zoomIdentity
            .translate(
              clientWidth / 2,
              clientHeight / 2)
      } yield zoomIdentity)
        .foreach(zoomBehavior
          .transform(
            d3.select(thisNode.ref),
            _))
    }

  val nodeStatesVar = {
    import ExperienceBrief.{ Blank, Employment, Skill }

    Var(experiences
      .descriptions
      .map {
        case _: Blank => ???
        case employment: Employment =>
          employment
            .into[ExperienceNodeState]
            .withFieldConst(_.x, 0d)
            .withFieldConst(_.y, 0d)
            .transform
        case skill: Skill =>
          skill
            .into[ExperienceNodeState]
            .withFieldConst(_.x, 0d)
            .withFieldConst(_.y, 0d)
            .transform
      })
  }

  val $nodeRenders: Signal[Seq[ReactiveSvgElement[G]]] =
    nodeStatesVar
      .signal
      .split(_.id)(ExperienceNodeView.render(_, _, _))

}
