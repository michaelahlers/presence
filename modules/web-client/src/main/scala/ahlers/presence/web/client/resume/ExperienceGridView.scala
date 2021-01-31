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

  val $nodeRenders: Signal[Seq[ReactiveSvgElement[G]]] =
    nodeStatesVar
      .signal
      .split(_.id)(ExperienceNodeView.render(_, _, _))

}
