package ahlers.presence.web.client

import ahlers.presence.web.client.resume._
import cats.instances.option._
import cats.syntax.apply._
import cats.syntax.option._
import com.raquo.airstream.core.Observer
import com.raquo.airstream.signal.Var
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.{ ReactiveElement, ReactiveSvgElement }
import d3v4._
import d3v4.d3.ZoomBehavior
import d3v4.d3force._
import d3v4.d3zoom.{ Transform, ZoomEvent }
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumePage {

  val zb: ZoomBehavior[dom.EventTarget] = d3.zoom()

  case class onZoom(behavior: ZoomBehavior[dom.EventTarget]) {

    @inline def -->(observer: Observer[ZoomEvent]): Binder[ReactiveElement.Base] =
      ReactiveElement.bindSubscription(_) { context =>
        val selection = d3.select(context.thisNode.ref)

        behavior
          .scaleExtent(Seq(1d, 10d).toJSArray)
          .on("zoom", () => observer.onNext(d3.event))
          .apply(selection)

        new Subscription(
          context.owner,
          cleanup = () =>
            behavior
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
    val nodeRadiusVar: Var[Double] = Var(50d)
    val $nodeRadius = nodeRadiusVar.signal

    //val linkDistanceVar = Var(50d)
    //val linkStrengthVar = Var(0.1d)
    val chargeStrengthVar = Var(1d)
    val centeringXVar = Var(0)
    val centeringYVar = Var(0)

    //val focusedNodeVar: Var[Option[ExperienceNodeUi]] = Var(none)
    //val $focusedNode: Signal[Option[ExperienceNodeUi]] = focusedNodeVar.signal

    val diagram = {
      import svg._

      val transformDiagramVar: Var[Option[Transform]] = Var(none)

      val focusedNodeVar: Var[Option[ExperienceNodeUi]] = Var(none)
      val $focusedNode: Signal[Option[ExperienceNodeUi]] = focusedNodeVar.signal

      $focusedNode.foreach(println(_))(unsafeWindowOwner)

      svg(
        className := "d-flex flex-grow-1 bg-dark",
        onZoom(zb) --> transformDiagramVar.writer.contramap[ZoomEvent](_.transform.some),
        onClick.mapToValue(none) --> focusedNodeVar.writer,
        g(
          transform <-- transformDiagramVar.signal.map(_.fold("")(_.toString())),
          //experiences.links.map(link =>
          //  link.render(
          //    $focusedNode)),
          experiences.nodes.map(node =>
            node.render(
              $nodeRadius,
              onClick.map(_.stopPropagation()).mapToValue(node.some) --> focusedNodeVar.writer))
          //$focusedNode,
          //onClick.mapToValue(node.some) --> focusedNodeVar.writer))
        ),
        inContext { thisNode =>
          val $width =
            windowEvents
              .onResize
              .mapTo(thisNode.ref.clientWidth)
              .toSignal(thisNode.ref.clientWidth)

          val $height =
            windowEvents
              .onResize
              .mapTo(thisNode.ref.clientHeight)
              .toSignal(thisNode.ref.clientHeight)

          //val arrange = {
          //  val theta = Math.PI * (3 - Math.sqrt(5))
          //
          //  for {
          //    nodeRadius <- $nodeRadius
          //    width <- $width
          //    height <- $height
          //  } yield {
          //    val step = nodeRadius.getOrElse(0d) * 1.5d
          //    experiences
          //      .nodes
          //      .zipWithIndex
          //      .foreach { case (node, i) =>
          //        val radius = step * Math.sqrt(i + 0.5d)
          //        val a = theta * (i + 0.5d)
          //        node.x = width / 2 + radius * Math.cos(a)
          //        node.y = height / 2 + radius * Math.sin(a)
          //      }
          //  }
          //}

          //val $transform =
          //  for {
          //    xF <- $focusedNode.flatMap(_.map(_.$x).getOrElse(Val(none)))
          //    yF <- $focusedNode.flatMap(_.map(_.$y).getOrElse(Val(none)))
          //  } yield {
          //    val width = thisNode.ref.clientWidth
          //    val height = thisNode.ref.clientHeight
          //    (xF, yF)
          //      .mapN((x, y) =>
          //        d3.zoomIdentity
          //          .translate(width / 2, height / 2)
          //          .scale(2)
          //          .translate(-x, -y))
          //      .getOrElse(d3.zoomIdentity)
          //  }

          val $transform =
            for {
              centerX <- centeringXVar.signal
              centerY <- centeringYVar.signal
              focusedNodeF <- $focusedNode
            } yield focusedNodeF match {
              case None => d3.zoomIdentity
              case Some(focusedNode) =>
                val xF = focusedNode.$x.now()
                val yF = focusedNode.$y.now()
                (xF, yF)
                  .mapN((x, y) =>
                    d3.zoomIdentity
                      .translate(centerX, centerY)
                      .scale(2)
                      .translate(-x, -y))
                  .getOrElse(d3.zoomIdentity)
            }

          $transform --> (zb.transform(d3.select(thisNode.ref), _)) ::
            $width.map(_ / 2) --> centeringXVar.writer ::
            $height.map(_ / 2) --> centeringYVar.writer ::
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

          centeringXVar.set(thisNode.ref.clientWidth / 2)
          centeringYVar.set(thisNode.ref.clientHeight / 2)
        }
      )
    }

    //val link: Link[ExperienceNodeUi, ExperienceLinkUi] =
    //  d3.forceLink[ExperienceNodeUi, ExperienceLinkUi](js.Array()) //experiences.links.toJSArray)
    //    .distance(linkDistanceVar.now())
    //    .strength(linkStrengthVar.now())

    //$focusedNode.foreach {
    //  case Some(focusedNode) => link.links(experiences.adjacentLinks(focusedNode).toJSArray)
    //  case _ => link.links(js.Array())
    //}(unsafeWindowOwner)

    val charge: ManyBody[ExperienceNodeUi] =
      d3.forceManyBody()
        .strength(chargeStrengthVar.now())

    val centering: Centering[ExperienceNodeUi] =
      d3.forceCenter(centeringXVar.now(), centeringXVar.now())

    val centerX: PositioningX[ExperienceNodeUi] =
      d3.forceX(centeringXVar.now()).strength(0.1d)

    val centerY: PositioningY[ExperienceNodeUi] =
      d3.forceY(centeringYVar.now()).strength(0.2d)

    val collisionStrength = Var(1d)
    val collision: Collision[ExperienceNodeUi] =
      d3.forceCollide()
        .strength(collisionStrength.now())
    //.radius { node =>
    //  println(node.experience.id + " has radius " + node.radiusVar.now())
    //  node.radiusVar.now().getOrElse(0d)
    //}

    val simulation: Simulation[ExperienceNodeUi] =
      d3.forceSimulation(experiences.nodes.toJSArray)
      //.force("link", link)
        .force("charge", charge)
        .force("center", centering)
        //.force("centerX", centerX)
        //.force("centerY", centerY)
        .force("collide", collision)

    val onEnterPress = onKeyPress.filter(_.keyCode == KeyCode.Enter)

    article(
      className := "d-flex flex-grow-1",
      diagram,
      //div(
      //  className := "container-fluid",
      //  div(
      //    className := "row",
      //    div(
      //      className := "col-12",
      //      span("Node Radius: "),
      //      input(
      //        value <-- $nodeRadius.map(_.fold("")(_.toString())),
      //        inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble.some) --> nodeRadiusVar.writer))
      //    ),
      //    //div(
      //    //  className := "col-12",
      //    //  span("Link Distance: "),
      //    //  input(
      //    //    value <-- linkDistanceVar.signal.map(_.toString),
      //    //    inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> linkDistanceVar.writer)),
      //    //  span("Link Strength: "),
      //    //  input(
      //    //    value <-- linkStrengthVar.signal.map(_.toString),
      //    //    inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> linkStrengthVar.writer))
      //    //),
      //    div(
      //      className := "row",
      //      div(
      //        className := "col-12",
      //        span("Charge Strength: "),
      //        input(
      //          value <-- chargeStrengthVar.signal.map(_.toString),
      //          inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> chargeStrengthVar.writer))
      //      )
      //    ),
      //    div(
      //      className := "row",
      //      div(
      //        className := "col-12",
      //        span("Collision Strength: "),
      //        input(
      //          value <-- collisionStrength.signal.map(_.toString),
      //          inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> collisionStrength.writer))
      //      )
      //    ),
      //    div(
      //      className := "row",
      //      div(
      //        className := "col-12",
      //        span("Centering X: "),
      //        input(
      //          value <-- centeringXVar.signal.map(_.toString),
      //          inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> centeringXVar.writer)),
      //        span("Centering Y: "),
      //        input(
      //          value <-- centeringYVar.signal.map(_.toString),
      //          inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> centeringYVar.writer))
      //      )
      //    )
      //  ),
      //),
      inContext { _ =>
        $nodeRadius --> (nodeRadius => collision.radius(_ => nodeRadius * 1.1d)) ::
          $nodeRadius.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          //linkDistanceVar.signal --> (link.distance(_)) ::
          //linkDistanceVar.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          //linkStrengthVar.signal --> (link.strength(_)) ::
          //linkStrengthVar.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          chargeStrengthVar.signal --> (charge.strength(_)) ::
          chargeStrengthVar.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          collisionStrength.signal --> (collision.strength(_)) ::
          collisionStrength.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringXVar.signal --> (centering.x(_)) ::
          centeringXVar.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringYVar.signal --> (centering.y(_)) ::
          centeringYVar.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringXVar.signal --> (centerX.x(_)) ::
          centeringXVar.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          centeringYVar.signal --> (centerY.y(_)) ::
          centeringYVar.signal.mapToValue(1d) --> (simulation.alphaTarget(_).restart()) ::
          Nil
      }
    )

  }

  implicit class ExperienceLinkSyntax(private val link: ExperienceLinkUi) extends AnyVal {

    @inline def render(
      $focusedNode: Signal[Option[ExperienceNodeUi]]
    ) = {
      import svg._

      val $display =
        $focusedNode.map {
          case Some(focusedNode) if experiences.adjacentLinks(focusedNode).contains(link) => "inline"
          case _ => "none"
        }

      line(
        display <-- $display,
        stroke := "black",
        x1 <-- link.source.$x.map(_.fold("")(_.toString)),
        y1 <-- link.source.$y.map(_.fold("")(_.toString)),
        x2 <-- link.target.$x.map(_.fold("")(_.toString)),
        y2 <-- link.target.$y.map(_.fold("")(_.toString))
      )
    }

  }

  implicit class ExperienceNodeSyntax(private val node: ExperienceNodeUi) extends AnyVal {

    @inline def render(
      $nodeRadius: Signal[Double],
      //$focusedNode: Signal[Option[ExperienceNodeUi]],
      modifiers: Modifier[ReactiveSvgElement[dom.raw.SVGElement]]*
    ) = {
      import svg._

      //val $radius: Signal[Double] =
      //  $focusedNode.flatMap {
      //    case None => $nodeRadius
      //    case Some(focusedNode) if focusedNode == node => $nodeRadius.map(_ * 1.2d)
      //    case Some(focusedNode) if experiences.adjacentNodes(focusedNode).contains(node) => $nodeRadius.map(_ * 1.1d)
      //    case Some(_) => $nodeRadius.map(_ / 2d)
      //  }

      //val $fx =
      //  for {
      //    focusedNode <- $focusedNode
      //    x <- node.$x
      //  } yield
      //    if (focusedNode.contains(node)) x
      //    else none

      //val $fy =
      //  for {
      //    focusedNode <- $focusedNode
      //    y <- node.$y
      //  } yield
      //    if (focusedNode.contains(node)) y
      //    else none

      //val transformNodeVar: Var[Transform] = Var(d3.zoomIdentity)
      g(
        //transform <-- transformNodeVar.signal.map(_.toString()),
        circle(
          //r <-- node.$radius.map(_.fold("")(_.toString)),
          r <-- $nodeRadius.map(_.toString),
          cx <-- node.$x.map(_.fold("")(_.toString)),
          cy <-- node.$y.map(_.fold("")(_.toString)),
          fill := (node.experience match {
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
          node.experience.id.toText + " " + experiences.adjacentNodes(node).map(_.experience.id.toText).mkString("[", ", ", "]")
        ),
        //inContext { context =>
        //  $radius.map(_.some) --> node.radiusVar.writer ::
        //    $fx --> node.fxVar.writer ::
        //    $fy --> node.fyVar.writer ::
        //    Nil
        //},
        //onMountCallback { context =>
        //  import context.thisNode
        //
        //  d3.zoom()
        //    .on("zoom", () => transformNodeVar.set(d3.event.transform))
        //    .scaleBy(d3.select(thisNode.ref), 1.1d)
        //
        //},
        //onClick.mapToValue(node.some) --> focusedExperienceVar
        modifiers
      )
    }

  }

}
