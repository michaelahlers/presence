package ahlers.presence.web.client

import cats.syntax.option._
import com.raquo.laminar.api.L._
import d3v4._
import d3v4.d3force.{ Centering, Collision, Force, Link, ManyBody }
import org.scalajs.dom.ext.KeyCode

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumePage {

  case class SimNode(
    _index: Var[Option[Index]],
    _x: Var[Option[Double]],
    _y: Var[Option[Double]],
    _vx: Var[Option[Double]],
    _vy: Var[Option[Double]],
    _fx: Var[Option[Double]],
    _fy: Var[Option[Double]])
    extends SimulationNode {

    val $index = _index.signal
    override def index = _index.now().orUndefined
    override def index_=(index: UndefOr[Index]) = _index.set(index.toOption)

    val $x = _x.signal
    override def x = _x.now().orUndefined
    override def x_=(x: UndefOr[Double]) = _x.set(x.toOption)

    val $y = _y.signal
    override def y = _y.now().orUndefined
    override def y_=(y: UndefOr[Double]) = _y.set(y.toOption)

    val $vx = _vx.signal
    override def vx = _vx.now().orUndefined
    override def vx_=(vx: UndefOr[Double]) = _vx.set(vx.toOption)

    val $vy = _vy.signal
    override def vy = _vy.now().orUndefined
    override def vy_=(vy: UndefOr[Double]) = _vy.set(vy.toOption)

    val $fx = _fx.signal
    override def fx = _fx.now().orUndefined
    override def fx_=(fx: UndefOr[Double]) = _fx.set(fx.toOption)

    val $fy = _fy.signal
    override def fy = _fy.now().orUndefined
    override def fy_=(fy: UndefOr[Double]) = _fy.set(fy.toOption)

  }

  object SimNode {

    def apply(
      index: Option[Index],
      x: Option[Double],
      y: Option[Double],
      vx: Option[Double],
      vy: Option[Double],
      fx: Option[Double],
      fy: Option[Double]
    ): SimNode =
      SimNode(
        Var(index),
        Var(x),
        Var(y),
        Var(vx),
        Var(vy),
        Var(fx),
        Var(fy))

    def apply(
      index: Index,
      x: Double,
      y: Double,
      vx: Double,
      vy: Double,
      fx: Double,
      fy: Double
    ): SimNode =
      SimNode(
        index.some,
        x.some,
        y.some,
        vx.some,
        vy.some,
        fx.some,
        fy.some)

  }

  case class SimLink(
    _index: Var[Option[Index]],
    source: SimNode,
    target: SimNode)
    extends SimulationLink[SimNode, SimNode] {

    override def index = _index.now().orUndefined
    override def index_=(index: UndefOr[Index]) = _index.set(index.toOption)

  }

  object SimLink {

    def apply(
      index: Option[Index],
      source: SimNode,
      target: SimNode
    ): SimLink =
      SimLink(
        Var(index),
        source,
        target)

    def apply(
      index: Index,
      source: SimNode,
      target: SimNode
    ): SimLink =
      SimLink(
        index.some,
        source,
        target)

  }

  def apply(): Div = {
    val nodes: Seq[SimNode] =
      (0 until 5)
        .map(_.some)
        .map(SimNode(_, 100d.some, 300d.some, none, none, none, none))

    val links: Seq[SimLink] =
      SimLink(0.some, nodes(0), nodes(1)) ::
        SimLink(2.some, nodes(0), nodes(2)) ::
        SimLink(1.some, nodes(1), nodes(2)) ::
        Nil

    val illustration = {
      import svg._

      svg(
        width := "100%",
        height := "100%",
        g(
          links.map(link =>
            line(
              style := "stroke: #aaa",
              x1 <-- link.source.$x.map(_.fold("")(_.toString)),
              y1 <-- link.source.$y.map(_.fold("")(_.toString)),
              x2 <-- link.target.$x.map(_.fold("")(_.toString)),
              y2 <-- link.target.$y.map(_.fold("")(_.toString))
            )),
          nodes.map(node =>
            circle(
              r := "20",
              cx <-- node.$x.map(_.fold("")(_.toString)),
              cy <-- node.$y.map(_.fold("")(_.toString)),
              fill := "#69b3a2"))
        )
      )
    }

    val linkDistance = Var(100d)
    val linkStrength = Var(0.1d)
    val link: Link[SimNode, SimLink] =
      d3.forceLink[SimNode, SimLink](links.toJSArray)
        .distance(linkDistance.now())
        .strength(linkStrength.now())

    val chargeStrength = Var(-100d)
    val charge: ManyBody[SimNode] =
      d3.forceManyBody()
        .strength(chargeStrength.now())

    val centeringX = Var(400)
    val centeringY = Var(300)
    val centering: Centering[SimNode] =
      d3.forceCenter(centeringX.now(), centeringY.now())

    val collision: Collision[SimNode] =
      d3.forceCollide()
        .strength(.2)
        .radius(_ => 30)

    val simulation =
      d3.forceSimulation(nodes.toJSArray)
        .force("link", link)
        .force("charge", charge)
        .force("center", centering)
    //.force("collide", collision)

    val onEnterPress = onKeyPress.filter(_.keyCode == KeyCode.Enter)

    div(
      width := "100%",
      height := "600px",
      className := "border border-3",
      illustration,
      div(
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
        span("Charge Strength: "),
        input(
          value <-- chargeStrength.signal.map(_.toString),
          inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> chargeStrength.writer))
      ),
      div(
        span("Centering X: "),
        input(
          value <-- centeringX.signal.map(_.toString),
          inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> centeringX.writer)),
        span("Centering Y: "),
        input(
          value <-- centeringY.signal.map(_.toString),
          inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toInt) --> centeringY.writer))
      ),
      onMountCallback { context =>
        import context.owner
        linkDistance.signal.foreach(link.distance(_))
        linkDistance.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

        linkStrength.signal.foreach(link.strength(_))
        linkStrength.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

        chargeStrength.signal.foreach(charge.strength(_))
        chargeStrength.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

        centeringX.signal.foreach(centering.x(_))
        centeringX.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

        centeringY.signal.foreach(centering.y(_))
        centeringY.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())
      }
      //inContext { el =>
      //  windowEvents.onResize.mapToValue(el.ref.clientWidth / 2) --> centeringX.writer ::
      //    windowEvents.onResize.mapToValue(el.ref.clientHeight / 2) --> centeringY.writer ::
      //    Nil
      //}
    )
  }

}
