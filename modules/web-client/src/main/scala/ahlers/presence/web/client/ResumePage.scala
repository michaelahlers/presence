package ahlers.presence.web.client

import ahlers.presence.web.client.resume._
import cats.syntax.option._
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
    val nodeRadiusVar = Var(50)
    val $nodeRadius = nodeRadiusVar.signal

    val linkDistance = Var(10d)
    val linkStrength = Var(0.01d)
    val chargeStrength = Var(1d)
    val centeringX = Var(0)
    val centeringY = Var(0)

    val focusedExperienceVar: Var[Option[ExperienceNodeUi]] = Var(none)
    val $focusedExperience = focusedExperienceVar.signal

    val $adjacentLinks: Signal[Seq[ExperienceLinkUi]] =
      $focusedExperience.map(_
        .map(node => experiences.links.filter(link => link.source == node || link.target == node))
        .getOrElse(Seq.empty))

    val diagram = {
      import svg._

      val transformViewVar: Var[Option[Transform]] = Var(none)

      val $adjacentLines: Signal[Seq[SvgElement]] =
        $adjacentLinks.map(_.toSeq.map(link =>
          line(
            style := "stroke: black",
            x1 <-- link.$source.flatMap(_.$x).map(_.fold("")(_.toString)),
            y1 <-- link.$source.flatMap(_.$y).map(_.fold("")(_.toString)),
            x2 <-- link.$target.flatMap(_.$x).map(_.fold("")(_.toString)),
            y2 <-- link.$target.flatMap(_.$y).map(_.fold("")(_.toString))
          )))
      //.toStreamOrSignalChanges

      svg(
        width := "100%",
        height := "100%",
        onZoom --> transformViewVar.writer.contramap[ZoomEvent](_.transform.some),
        g(
          transform <-- transformViewVar.signal.map(_.fold("")(_.toString())),
          children <-- $adjacentLines,
          experiences.nodes.map { node =>
            //val transformNodeVar: Var[Transform] = Var(d3.zoomIdentity)
            g(
              //transform <-- transformNodeVar.signal.map(_.toString()),
              circle(
                r <-- (for {
                  r <- $nodeRadius
                  fn <- $focusedExperience
                } yield r - fn.filter(_ == node).fold(10)(_ => 0)).map(_.toString),
                cx <-- node.$x.map(_.fold("")(_.toString)),
                cy <-- node.$y.map(_.fold("")(_.toString)),
                fill := (node.payload match {
                  case _: ExperienceDescription.Skill => "blue"
                  case _: ExperienceDescription.Employment => "green"
                })
                //inContext { thisNode =>
                //  onMouseEnter.mapTo {
                //    println(d3.select(thisNode.ref))
                //    println(d3.zoom().scaleBy(d3.select(thisNode.ref), 1.5d))
                //    d3.zoom()
                //      .scaleBy(
                //        d3.select(thisNode.ref),
                //        1.5d)
                //  } --> transformNodeVar.writer
                //}
              ),
              text(
                x <-- node.$x.map(_.fold("")(_.toString())),
                y <-- node.$y.map(_.fold("")(_.toString())),
                style := "15px sans-serif",
                node.payload.id.toText
              ),
              //onMountCallback { context =>
              //  import context.thisNode
              //
              //  val zoom: ZoomBehavior[dom.EventTarget] =
              //    d3.zoom()
              //      .on("zoom", () => transformNodeVar.set(d3.event.transform))
              //
              //  zoom.scaleBy(d3.select(thisNode.ref), 1.1d)
              //
              //}
              onClick.mapToValue(node.some) --> focusedExperienceVar
            )
          }
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
          import context.thisNode

          //val circle: Selection[dom.EventTarget] =
          //  d3.select(thisNode.ref)
          //    .append("circle")
          //    .attr("r", 20)
          //    .attr("cx", 100)
          //    .attr("cy", 100)
          //    .attr("fill", "red")

          //val zoom: ZoomBehavior[dom.EventTarget] =
          //  d3.zoom()
          //    .on("zoom", () => circle.attr("transform", d3.event.transform.toString()))

          //zoom.scaleBy(circle, 3.0d)

          centeringX.set(thisNode.ref.clientWidth / 2)
          centeringY.set(thisNode.ref.clientHeight / 2)
        }
      )
    }

    val link: Link[ExperienceNodeUi, ExperienceLinkUi] =
      d3.forceLink[ExperienceNodeUi, ExperienceLinkUi](js.Array()) //experiences.links.toJSArray)
        .distance(linkDistance.now())
        .strength(linkStrength.now())

    $adjacentLinks
      .map(_.toJSArray)
      .foreach(link.links(_))(unsafeWindowOwner)

    val charge: ManyBody[ExperienceNodeUi] =
      d3.forceManyBody()
        .strength(chargeStrength.now())

    //val centering: Centering[ExperienceNodeUi] =
    //  d3.forceCenter(centeringX.now(), centeringY.now())

    val centerX: PositioningX[ExperienceNodeUi] =
      d3.forceX(centeringX.now()).strength(0.05d)

    val centerY: PositioningY[ExperienceNodeUi] =
      d3.forceY(centeringY.now()).strength(0.05d)

    val collisionStrength = Var(1d)
    val collision: Collision[ExperienceNodeUi] =
      d3.forceCollide()
        .strength(collisionStrength.now())
        .radius { node =>
          val r = $nodeRadius.now()
          val fn = $focusedExperience.now()
          r + fn.filter(_ == node).fold(10)(_ => 0)
        }

    val simulation =
      d3.forceSimulation(experiences.nodes.toJSArray)
        .force("link", link)
        .force("charge", charge)
        //.force("center", centering)
        .force("centerX", centerX)
        .force("centerY", centerY)
        .force("collide", collision)

    val onEnterPress = onKeyPress.filter(_.keyCode == KeyCode.Enter)

    article(
      className := "container-fluid",
      div(
        className := "row",
        div(
          height := "600px",
          className := "col-12",
          diagram)),
      div(
        className := "row",
        div(
          className := "col-12",
          span("Node Radius: "),
          input(
            value <-- $nodeRadius.map(_.toString),
            inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> nodeRadiusVar.writer))
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
        $nodeRadius --> (nodeRadius => collision.radius(_ => nodeRadius + 10)) ::
          $nodeRadius.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          linkDistance.signal --> (link.distance(_)) ::
          linkDistance.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          linkStrength.signal --> (link.strength(_)) ::
          linkStrength.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          chargeStrength.signal --> (charge.strength(_)) ::
          chargeStrength.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          collisionStrength.signal --> (collision.strength(_)) ::
          collisionStrength.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          //centeringX.signal --> (centering.x(_)) ::
          //centeringX.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          //centeringY.signal --> (centering.y(_)) ::
          //centeringY.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringX.signal --> (centerX.x(_)) ::
          centeringX.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringY.signal --> (centerY.y(_)) ::
          centeringY.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          Nil
      }
    )
  }

}
