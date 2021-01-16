package ahlers.presence.web.client

import ahlers.presence.web.client.resume._
import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.Var
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveElement
import d3.laminar.SimulationLinkRx
import d3v4._
import d3v4.d3force._
import d3v4.d3zoom.{ Transform, ZoomEvent }
import org.scalajs.dom.ext.KeyCode

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumePage {

  object onZoom {

    @inline def -->(observer: Observer[ZoomEvent]): Binder[ReactiveElement.Base] =
      ReactiveElement.bindSubscription(_) { context =>
        val selection = d3.select(context.thisNode.ref)

        d3.zoom()
          .on("zoom", () => observer.onNext(d3.event))
          .apply(selection)

        new Subscription(
          context.owner,
          cleanup = () =>
            d3.zoom()
              .on("zoom", null)
              .apply(selection))
      }

    // And so on…

    //@inline def -->(onNext: ZoomEvent => Unit): Binder[ReactiveElement.Base] =
    //  -->(Observer(onNext))

    //@inline def -->(eventBus: EventBus[ZoomEvent]): Binder[ReactiveElement.Base] =
    //  -->(eventBus.writer)

    //@inline def -->(targetVar: Var[ZoomEvent]): Binder[ReactiveElement.Base] =
    //  -->(targetVar.writer)

  }

  def apply(): HtmlElement = {
    val nodeRadius = Var(50)
    val linkDistance = Var(10d)
    val linkStrength = Var(0.01d)
    val chargeStrength = Var(1d)
    val centeringX = Var(0)
    val centeringY = Var(0)

    //val hoverIds = Var(Set.empty[ExperienceId])

    val illustration = {
      import svg._

      val transformVar: Var[Transform] = Var(d3zoom.zoomIdentity)

      svg(
        width := "100%",
        height := "100%",
        onZoom --> transformVar.writer.contramap[ZoomEvent](_.transform),
        g(
          transform <-- transformVar.signal.map(_.toString()),
          experiences.links.map(link =>
            line(
              style := "stroke: black",
              x1 <-- link.$source.flatMap(_.$x).map(_.fold("")(_.toString)),
              y1 <-- link.$source.flatMap(_.$y).map(_.fold("")(_.toString)),
              x2 <-- link.$target.flatMap(_.$x).map(_.fold("")(_.toString)),
              y2 <-- link.$target.flatMap(_.$y).map(_.fold("")(_.toString))
            )),
          experiences.nodes.map(node =>
            g(
              circle(
                r <-- nodeRadius.signal.map(_.toString()),
                cx <-- node.$x.map(_.fold("")(_.toString)),
                cy <-- node.$y.map(_.fold("")(_.toString)),
                fill := (node.payload match {
                  case _: ExperienceDescription.Skill => "blue"
                  case _: ExperienceDescription.Employment => "green"
                })
              ),
              text(
                x <-- node.$x.map(_.fold("")(_.toString())),
                y <-- node.$y.map(_.fold("")(_.toString())),
                style := "15px sans-serif",
                node.payload.id.toText
              ) //,
              //onMouseEnter.mapTo(hoverIds.now() + node.payload.id) --> hoverIds.writer,
              //onMouseLeave.mapTo(hoverIds.now() - node.payload.id) --> hoverIds.writer
            ))
        ),
        inContext { thisNode =>
          val $width =
            windowEvents
              .onResize
              .mapTo(thisNode.ref.clientWidth)

          val $height =
            windowEvents
              .onResize
              .mapTo(thisNode.ref.clientHeight)

          $width.map(_ / 2) --> centeringX.writer ::
            $height.map(_ / 2) --> centeringY.writer ::
            Nil
        },
        onMountCallback { context =>
          centeringX.set(context.thisNode.ref.clientWidth / 2)
          centeringY.set(context.thisNode.ref.clientHeight / 2)
        }
      )
    }

    val link: Link[ExperienceNodeUi, SimulationLinkRx[ExperienceNodeUi, ExperienceNodeUi]] =
      d3.forceLink[ExperienceNodeUi, SimulationLinkRx[ExperienceNodeUi, ExperienceNodeUi]](js.Array()) //experiences.links.toJSArray)
        .distance(linkDistance.now())
        .strength(linkStrength.now())

    val charge: ManyBody[ExperienceNodeUi] =
      d3.forceManyBody()
        .strength(chargeStrength.now())

    val centering: Centering[ExperienceNodeUi] =
      d3.forceCenter(centeringX.now(), centeringY.now())

    val centerX: PositioningX[ExperienceNodeUi] =
      d3.forceX(centeringX.now()).strength(0.2d)

    val centerY: PositioningY[ExperienceNodeUi] =
      d3.forceY(centeringY.now()).strength(0.3d)

    val collisionStrength = Var(1d)
    val collision: Collision[ExperienceNodeUi] =
      d3.forceCollide()
        .strength(collisionStrength.now())
        .radius(_ => nodeRadius.now() + 10)

    val simulation =
      d3.forceSimulation(experiences.nodes.toJSArray)
      //.force("link", link)
        .force("charge", charge)
        .force("center", centering)
        //.force("foo", centerX)
        //.force("bear", centerY)
        .force("collide", collision)

    val onEnterPress = onKeyPress.filter(_.keyCode == KeyCode.Enter)

    article(
      className := "container-fluid",
      div(
        className := "row",
        div(
          height := "600px",
          className := "col-12",
          illustration)),
      div(
        className := "row",
        div(
          className := "col-12",
          span("Node Radius: "),
          input(
            value <-- nodeRadius.signal.map(_.toString),
            inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> nodeRadius.writer))
        ),
        div(
          className := "col-12",
          span("Link Distance: "),
          input(
            value <-- linkDistance.signal.map(_.toString),
            inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> linkDistance.writer)),
          span("Link Strength: "),
          input(
            value <-- linkStrength.signal.map(_.toString),
            inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> linkStrength.writer))
        ),
        div(
          className := "row",
          div(
            className := "col-12",
            span("Charge Strength: "),
            input(
              value <-- chargeStrength.signal.map(_.toString),
              inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> chargeStrength.writer))
          )
        ),
        div(
          className := "row",
          div(
            className := "col-12",
            span("Collision Strength: "),
            input(
              value <-- collisionStrength.signal.map(_.toString),
              inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> collisionStrength.writer))
          )
        ),
        div(
          className := "row",
          div(
            className := "col-12",
            span("Centering X: "),
            input(
              value <-- centeringX.signal.map(_.toString),
              inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> centeringX.writer)),
            span("Centering Y: "),
            input(
              value <-- centeringY.signal.map(_.toString),
              inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> centeringY.writer))
          )
        )
      ),
      inContext { _ =>
        nodeRadius.signal --> (nodeRadius => collision.radius(_ => nodeRadius + 10)) ::
          nodeRadius.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          linkDistance.signal --> (link.distance(_)) ::
          linkDistance.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          linkStrength.signal --> (link.strength(_)) ::
          linkStrength.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          chargeStrength.signal --> (charge.strength(_)) ::
          chargeStrength.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          collisionStrength.signal --> (collision.strength(_)) ::
          collisionStrength.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringX.signal --> (centering.x(_)) ::
          centeringX.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringY.signal --> (centering.y(_)) ::
          centeringY.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringX.signal --> (centerX.x(_)) ::
          centeringX.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringY.signal --> (centerY.y(_)) ::
          centeringY.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          Nil
      }
    )
  }

}
