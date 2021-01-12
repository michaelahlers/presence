package ahlers.presence.web.client

import ahlers.presence.web.client.resume._
import com.raquo.laminar.api.L._
import d3v4._
import d3v4.d3force.{ Centering, Collision, Link, ManyBody }
import org.scalajs.dom.ext.KeyCode

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumePage {

  object experiences {

    val Akka = ExperienceDescription.skill(ExperienceId("akka"), ExperienceName("Akka"))
    val Bootstrap = ExperienceDescription.skill(ExperienceId("bootstrap"), ExperienceName("Bootstrap"))
    val CSS = ExperienceDescription.skill(ExperienceId("css"), ExperienceName("CSS"))
    val Flyway = ExperienceDescription.skill(ExperienceId("flyway"), ExperienceName("Flyway"))
    val SBT = ExperienceDescription.skill(ExperienceId("sbt"), ExperienceName("SBT"))
    val Scala = ExperienceDescription.skill(ExperienceId("scala"), ExperienceName("Scala"))
    val Slick = ExperienceDescription.skill(ExperienceId("slick"), ExperienceName("Slick"))
    val Lagom = ExperienceDescription.skill(ExperienceId("lagom"), ExperienceName("Lagom"))
    val PlayFramework = ExperienceDescription.skill(ExperienceId("play-framework"), ExperienceName("Play Framework"))
    val PostgreSQL = ExperienceDescription.skill(ExperienceId("postgresql"), ExperienceName("PostgreSQL"))

    val LiveSafe =
      ExperienceDescription.employment(
        ExperienceId("livesafe"),
        ExperienceDescription.Employment.Company(
          "LiveSafe",
          "Rosslyn",
          "Virginia"))

    val ThompsonReutersSpecialServices =
      ExperienceDescription.employment(
        ExperienceId("trss"),
        ExperienceDescription.Employment.Company(
          "Thompson-Reuters Special Services",
          "McLean",
          "Virginia"))

    val VerizonBusiness =
      ExperienceDescription.employment(
        ExperienceId("verizon-business"),
        ExperienceDescription.Employment.Company(
          "Verizon Business",
          "Ashburn",
          "Virginia"))

    val descriptions: Seq[ExperienceDescription] =
      Akka ::
        Bootstrap ::
        CSS ::
        Flyway ::
        Lagom ::
        PlayFramework ::
        PostgreSQL ::
        SBT ::
        Scala ::
        Slick ::
        LiveSafe ::
        ThompsonReutersSpecialServices ::
        VerizonBusiness ::
        Nil

    val nodes: Seq[SimulationNodeRx[ExperienceDescription]] =
      descriptions
        .zipWithIndex
        .map { case (detail, index) =>
          SimulationNodeRx(index, detail)
        }

    val relationSets: Seq[Set[_ <: ExperienceRef]] =
      Set(Akka, Lagom, PlayFramework, Scala, Slick) ::
        Set(Bootstrap, CSS) ::
        Set(Flyway, PostgreSQL, Slick) ::
        Set(LiveSafe, Akka, Lagom, SBT, Scala) ::
        Set(ThompsonReutersSpecialServices, PlayFramework, SBT, Scala) ::
        Set(VerizonBusiness, Bootstrap, CSS, PlayFramework, SBT, Scala) ::
        Nil

    val links: Seq[SimulationLinkRx[SimulationNodeRx[ExperienceDescription], SimulationNodeRx[ExperienceDescription]]] = {
      val byId: Map[ExperienceId, SimulationNodeRx[ExperienceDescription]] =
        nodes
          .groupBy(_.payload.id)
          .view.mapValues(_.head)
          .toMap

      val (relations, _) =
        (for {
          rs <- relationSets
          a <- rs
          b <- rs
          if a != b
        } yield (a, b))
          .foldLeft((List.empty[(ExperienceRef, ExperienceRef)], Set.empty[(ExperienceRef, ExperienceRef)])) {
            case (a @ (_, visits), r) if visits(r) => a
            case ((a, visits), r) => (r :: a, visits + r + r.swap)
          }

      relations
        .zipWithIndex
        .map { case ((a, b), index) =>
          SimulationLinkRx(index, byId(a.id), byId(b.id))
        }
    }

  }

  def apply(): HtmlElement = {
    val nodeRadius = Var(50)
    val linkDistance = Var(10d)
    val linkStrength = Var(0.01d)
    val chargeStrength = Var(1d)
    val centeringX = Var(400)
    val centeringY = Var(300)

    val illustration = {
      import svg._

      svg(
        width := "100%",
        height := "100%",
        g(
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
                r <-- nodeRadius.signal.map(_.toString),
                cx <-- node.$x.map(_.fold("")(_.toString)),
                cy <-- node.$y.map(_.fold("")(_.toString)),
                fill := (node.payload match {
                  case _: ExperienceDescription.Skill => "blue"
                  case _: ExperienceDescription.Employment => "green"
                })
              ),
              text(
                x <-- node.$x.map(_.fold("")(_.toString)),
                y <-- node.$y.map(_.fold("")(_.toString)),
                style := "15px sans-serif",
                node.payload.id.toText)
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
        }
      )
    }

    val link: Link[SimulationNodeRx[ExperienceDescription], SimulationLinkRx[SimulationNodeRx[ExperienceDescription], SimulationNodeRx[ExperienceDescription]]] =
      d3.forceLink[SimulationNodeRx[ExperienceDescription], SimulationLinkRx[SimulationNodeRx[ExperienceDescription], SimulationNodeRx[ExperienceDescription]]](js.Array()) //experiences.links.toJSArray)
        .distance(linkDistance.now())
        .strength(linkStrength.now())

    val charge: ManyBody[SimulationNodeRx[ExperienceDescription]] =
      d3.forceManyBody()
        .strength(chargeStrength.now())

    val centering: Centering[SimulationNodeRx[ExperienceDescription]] =
      d3.forceCenter(centeringX.now(), centeringY.now())

    val collisionStrength = Var(1d)
    val collision: Collision[SimulationNodeRx[ExperienceDescription]] =
      d3.forceCollide()
        .strength(collisionStrength.now())
        .radius(_ => nodeRadius.now() + 10)

    val simulation =
      d3.forceSimulation(experiences.nodes.toJSArray)
      //.force("link", link)
        .force("charge", charge)
        .force("center", centering)
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
          Nil
      }
    )
  }

}
