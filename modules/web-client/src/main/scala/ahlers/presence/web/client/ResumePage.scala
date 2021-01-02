package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import d3v4._
import cats.syntax.option._
import scala.scalajs.js
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
    override def vy = _y.now().orUndefined
    override def vy_=(vy: UndefOr[Double]) = _vy.set(vy.toOption)

    val $fx = _fx.signal
    override def fx = _fx.now().orUndefined
    override def fx_=(fx: UndefOr[Double]) = _fx.set(fx.toOption)

    val $fy = _fy.signal
    override def fy = _y.now().orUndefined
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
      SimNode(0, 0, 0, 0, 0, 0, 0) ::
        SimNode(1, 0, 0, 0, 0, 0, 0) ::
        Nil

    val links: Seq[SimLink] =
      SimLink(0, nodes(0), nodes(1)) ::
        Nil

    val illustration = {
      import svg._

      svg(
        width := "800px",
        height := "600px",
        g(
          nodes.map(node =>
            circle(
              r := "20",
              cx <-- node.$x.map(_.fold("")(_.toString)),
              cy <-- node.$y.map(_.fold("")(_.toString)),
              fill := "#69b3a2")),
          links.map(link =>
            line(
              style := "stroke: #aaa",
              x1 <-- link.source.$x.map(_.fold("")(_.toString)),
              y1 <-- link.source.$y.map(_.fold("")(_.toString)),
              x2 <-- link.target.$x.map(_.fold("")(_.toString)),
              y2 <-- link.target.$y.map(_.fold("")(_.toString))
            ))
        )
      )
    }

    val container =
      div(
        width := "800px",
        height := "600px",
        className := "border border-3",
        illustration)

    d3.forceSimulation(nodes.toJSArray)
      .force("charge", d3.forceManyBody().strength(-400))
      .force("center", d3.forceCenter(400, 300))

    div(
      h1("Resume"),
      container)
  }

  //val graphHeight = 450
  //val barWidth = 80
  //val barSeparation = 10
  //val maxData = 50
  //
  //val horizontalBarDistance = barWidth + barSeparation
  //val barHeightMultiplier = graphHeight / maxData;
  //
  //val c = d3.hcl("DarkSlateBlue")
  //
  //val rectXFun = (d: Int, i: Int) => i * horizontalBarDistance
  //val rectYFun = (d: Int) => graphHeight - d * barHeightMultiplier
  //val rectHeightFun = (d: Int) => d * barHeightMultiplier
  //val rectColorFun = (d: Int, i: Int) => c //c.brighter(i * 0.5).toString
  //
  //def apply(): Div = {
  //  val container =
  //    div(
  //      width := "100%",
  //      height := "450px",
  //      className := "border border-3")
  //
  //  val svg =
  //    d3
  //      .select(container.ref)
  //      .append("svg")
  //      .attr("width", "100%")
  //      .attr("height", "450px")
  //
  //  val sel =
  //    svg
  //      .selectAll("rect")
  //      .data(js.Array(8, 22, 31, 36, 48, 17, 25))
  //
  //  sel.enter()
  //    .append("rect")
  //    .attr("x", rectXFun)
  //    .attr("y", rectYFun)
  //    .attr("width", barWidth)
  //    .attr("height", rectHeightFun)
  //    .style("fill", "DarkSlateBlue")
  //
  //  div(
  //    h1("Resume"),
  //    container)
  //}

}
