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
    val nodeRadiusVar: Var[Double] = Var(20d)
    val $nodeRadius = nodeRadiusVar.signal

    val centeringXVar = Var(0)
    val $centerX = centeringXVar.signal

    val centeringYVar = Var(0)
    val $centerY = centeringYVar.signal

    val focusedNodeVar: Var[Option[ExperienceNodeUi]] = Var(none)
    val $focusedNode: Signal[Option[ExperienceNodeUi]] = focusedNodeVar.signal

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
              $centerX,
              $centerY,
              onClick.map(_.stopPropagation()).mapToValue(node.some.filterNot(_.experience == ExperienceDescription.Blank)) --> focusedNodeVar.writer
            ))
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

          val $transform =
            for {
              nodeRadius <- $nodeRadius
              centerX <- $centerX
              centerY <- $centerY
              focusedNodeF <- $focusedNode
            } yield focusedNodeF match {
              case None => d3.zoomIdentity
              case Some(focusedNode) =>
                val x = focusedNode.xFor(nodeRadius, centerX)
                val y = focusedNode.yFor(nodeRadius, centerY)
                d3.zoomIdentity
                  .translate(centerX, centerY)
                  .scale(2)
                  .translate(-x, -y)
            }

          $transform --> (zb.transform(d3.select(thisNode.ref), _)) ::
            $width.map(_ / 2) --> centeringXVar.writer ::
            $height.map(_ / 2) --> centeringYVar.writer ::
            Nil
        },
        onMountCallback { context =>
          import context.thisNode
          centeringXVar.set(thisNode.ref.clientWidth / 2)
          centeringYVar.set(thisNode.ref.clientHeight / 2)
        }
      )
    }

    val onEnterPress = onKeyPress.filter(_.keyCode == KeyCode.Enter)

    article(
      className := "d-flex flex-column flex-grow-1",
      diagram,
      div(
        className := "container-fluid",
        div(
          className := "row",
          div(
            className := "col-12",
            span("Node Radius: "),
            input(
              value <-- $nodeRadius.map(_.toString()),
              inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> nodeRadiusVar.writer))
          )
        )
      )
    )

  }

  //implicit class ExperienceLinkSyntax(private val link: ExperienceLinkUi) extends AnyVal {
  //
  //  @inline def render(
  //    $focusedNode: Signal[Option[ExperienceNodeUi]]
  //  ) = {
  //    import svg._
  //
  //    val $display =
  //      $focusedNode.map {
  //        case Some(focusedNode) if experiences.adjacentLinks(focusedNode).contains(link) => "inline"
  //        case _ => "none"
  //      }
  //
  //    line(
  //      display <-- $display,
  //      stroke := "black",
  //      x1 <-- link.source.$x.map(_.fold("")(_.toString)),
  //      y1 <-- link.source.$y.map(_.fold("")(_.toString)),
  //      x2 <-- link.target.$x.map(_.fold("")(_.toString)),
  //      y2 <-- link.target.$y.map(_.fold("")(_.toString))
  //    )
  //  }
  //
  //}

  implicit class ExperienceNodeSyntax(private val node: ExperienceNodeUi) extends AnyVal {

    def xFor(nodeRadius: Double, centerX: Double): Double = {
      val theta = Math.PI * (3 - Math.sqrt(5))
      val i = node.index
      val step = nodeRadius * 2d
      val radius = step * Math.sqrt(i + 0.25d)
      val a = theta * (i + 0.25d)
      centerX + radius * Math.cos(a)
    }

    def yFor(nodeRadius: Double, centerY: Double): Double = {
      val theta = Math.PI * (3 - Math.sqrt(5))
      val i = node.index
      val step = nodeRadius * 2d
      val radius = step * Math.sqrt(i + 0.25d)
      val a = theta * (i + 0.25d)
      centerY + radius * Math.sin(a)
    }

    @inline def render(
      $nodeRadius: Signal[Double],
      $centerX: Signal[Int],
      $centerY: Signal[Int],
      //$focusedNode: Signal[Option[ExperienceNodeUi]],
      modifiers: Modifier[ReactiveSvgElement[dom.raw.SVGElement]]*
    ) = {
      import svg._

      val $x =
        for {
          nodeRadius <- $nodeRadius
          centerX <- $centerX
        } yield xFor(nodeRadius, centerX)

      val $y =
        for {
          nodeRadius <- $nodeRadius
          centerY <- $centerY
        } yield yFor(nodeRadius, centerY)

      //val transformNodeVar: Var[Transform] = Var(d3.zoomIdentity)
      g(
        //transform <-- transformNodeVar.signal.map(_.toString()),
        circle(
          //r <-- node.$radius.map(_.fold("")(_.toString)),
          r <-- $nodeRadius.map(_.toString),
          cx <-- $x.map(_.toString()),
          cy <-- $y.map(_.toString()),
          fill := (node.experience match {
            case ExperienceDescription.Blank => "#333"
            case _: ExperienceDescription.Skill => "blue"
            case _: ExperienceDescription.Employment => "green"
          })
        ),
        //text(
        //  x <-- $x.map(_.toString()),
        //  y <-- $y.map(_.toString()),
        //  style := "15px sans-serif",
        //  node.experience.id.toText // + " " + experiences.adjacentNodes(node).map(_.experience.id.toText).mkString("[", ", ", "]")
        //),
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
