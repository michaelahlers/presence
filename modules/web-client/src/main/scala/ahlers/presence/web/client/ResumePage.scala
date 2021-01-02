package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import d3v4._

import scala.scalajs.js
import scala.scalajs.js.UndefOr

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumePage {

  case class SimNode(
    _index: Var[UndefOr[Index]],
    _x: Var[UndefOr[Double]],
    _y: Var[UndefOr[Double]],
    _vx: Var[UndefOr[Double]],
    _vy: Var[UndefOr[Double]],
    _fx: Var[UndefOr[Double]],
    _fy: Var[UndefOr[Double]])
    extends SimulationNode {

    override def index = _index.now()
    override def index_=(newIndex: UndefOr[Index]) = _index.set(newIndex)

    override def x = _x.now()
    override def x_=(newX: UndefOr[Double]) = _x.set()

    override def y = _y.now()
    override def y_=(newY: UndefOr[Double]) = _y.set()

    override def vx = _vx.now()
    override def vx_=(newVX: UndefOr[Double]) = _vx.set()

    override def vy = _y.now()
    override def vy_=(newVY: UndefOr[Double]) = _vy.set()

    override def fx = _fx.now()
    override def fx_=(newfX: UndefOr[Double]) = _fx.set()

    override def fy = _y.now()
    override def fy_=(newfY: UndefOr[Double]) = _fy.set()

  }

  def apply(): Div = {
    //val illustration = {
    //  import svg._
    //  svg(
    //    width := "800px",
    //    height := "600px")
    //}

    val container =
      div(
        width := "800px",
        height := "600px",
        className := "border border-3")

    val svg =
      d3.select(container.ref)
        .append("svg")
        .attr("width", 800)
        .attr("height", 600)
        .append("g")

    val links = data.links.asInstanceOf[js.Array[SimulationLink[SimulationNode, SimulationNode]]]

    val link =
      svg.selectAll("line")
        .data(links)
        .enter()
        .append("line")
        .style("stroke", "#aaa")

    val nodes = data.nodes.asInstanceOf[js.Array[SimulationNode]]

    val node =
      svg.selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr("r", 20)
        .style("fill", "#69b3a2")

//val simulation = {
//  val ticked: ListenerFunction0 = { () =>
//    link
//      .attr("x1", _.source.x)
//      .attr("y1", _.source.y)
//      .attr("x2", _.target.x)
//      .attr("y2", _.target.y)
//
//    node
//      .attr("cx", _.x.toString.toDouble + 6d)
//      .attr("cy", _.y.toString.toDouble + 6d)
//  }
//
//  d3.forceSimulation(nodes)
//    .force("link", d3.forceLink(links))
//    .force("charge", d3.forceManyBody().strength(-400))
//    .force("center", d3.forceCenter(400, 300))
//    .on("tick", ticked)
//}

    div(
      h1("Resume"),
      container)
  }

  val data = js.JSON.parse(
    """
      |{
      |  "nodes": [
      |    {
      |      "id": 1,
      |      "name": "A"
      |    },
      |    {
      |      "id": 2,
      |      "name": "B"
      |    },
      |    {
      |      "id": 3,
      |      "name": "C"
      |    },
      |    {
      |      "id": 4,
      |      "name": "D"
      |    },
      |    {
      |      "id": 5,
      |      "name": "E"
      |    },
      |    {
      |      "id": 6,
      |      "name": "F"
      |    },
      |    {
      |      "id": 7,
      |      "name": "G"
      |    },
      |    {
      |      "id": 8,
      |      "name": "H"
      |    },
      |    {
      |      "id": 9,
      |      "name": "I"
      |    },
      |    {
      |      "id": 10,
      |      "name": "J"
      |    }
      |  ],
      |  "links": [
      |
      |    {
      |      "source": 1,
      |      "target": 2
      |    },
      |    {
      |      "source": 1,
      |      "target": 5
      |    },
      |    {
      |      "source": 1,
      |      "target": 6
      |    },
      |
      |    {
      |      "source": 2,
      |      "target": 3
      |    },
      |            {
      |      "source": 2,
      |      "target": 7
      |    }
      |    ,
      |
      |    {
      |      "source": 3,
      |      "target": 4
      |    },
      |     {
      |      "source": 8,
      |      "target": 3
      |    }
      |    ,
      |    {
      |      "source": 4,
      |      "target": 5
      |    }
      |    ,
      |
      |    {
      |      "source": 4,
      |      "target": 9
      |    },
      |    {
      |      "source": 5,
      |      "target": 10
      |    }
      |  ]
      |}
      |""".stripMargin)

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
