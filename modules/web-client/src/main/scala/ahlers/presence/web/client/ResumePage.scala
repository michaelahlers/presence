package ahlers.presence.web.client

import ahlers.presence.web.client.resume.{ ExperienceLink, ExperienceNode }
import cats.syntax.option._
import com.raquo.laminar.api.L._
import d3v4._
import d3v4.d3force.{ Centering, Collision, Force, Link, ManyBody }
import org.scalajs.dom.ext.KeyCode

import java.util.concurrent.atomic.AtomicInteger
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumePage {

  object experiences {

    private val nextIndex: () => Int = {
      val next = new AtomicInteger()
      () => next.getAndIncrement()
    }

    val `Akka` = ExperienceNode.skill("akka", "Akka", nextIndex().some)
    val `Bootstrap` = ExperienceNode.skill("bootstrap", "Bootstrap", nextIndex().some)
    val `Cascading Style Sheets` = ExperienceNode.skill("css", "CSS", nextIndex().some)
    val `Flyway` = ExperienceNode.skill("flyway", "Flyway", nextIndex().some)
    val `SBT` = ExperienceNode.skill("sbt", "SBT", nextIndex().some)
    val `Scala` = ExperienceNode.skill("scala", "Scala", nextIndex().some)
    val `Slick` = ExperienceNode.skill("slick", "Slick", nextIndex().some)
    val `Lagom` = ExperienceNode.skill("lagom", "Lagom", nextIndex().some)
    val `Play Framework` = ExperienceNode.skill("play-framework", "Play Framework", nextIndex().some)
    val `PostgreSQL` = ExperienceNode.skill("postgresql", "PostgreSQL", nextIndex().some)

    val `LiveSafe` =
      ExperienceNode.employment(
        "livesafe",
        ExperienceNode.Employment.Company(
          "LiveSafe",
          "Rosslyn",
          "Virginia"),
        nextIndex().some)

    val `Thompson-Reuters Special Services` =
      ExperienceNode.employment(
        "trss",
        ExperienceNode.Employment.Company(
          "Thompson-Reuters Special Services",
          "McLean",
          "Virginia"),
        nextIndex().some)

    val `Verizon Business` =
      ExperienceNode.employment(
        "verizon-business",
        ExperienceNode.Employment.Company(
          "Verizon Business",
          "Ashburn",
          "Virginia"),
        nextIndex().some)

    val nodes: Seq[ExperienceNode] =
      `Akka` ::
        `Bootstrap` ::
        `Cascading Style Sheets` ::
        `Flyway` ::
        `Lagom` ::
        `Play Framework` ::
        `PostgreSQL` ::
        `SBT` ::
        `Scala` ::
        `Slick` ::
        `LiveSafe` ::
        `Thompson-Reuters Special Services` ::
        `Verizon Business` ::
        Nil

  }

  val links = {
    import experiences._

    val nextIndex: () => Int = {
      val next = new AtomicInteger()
      () => next.getAndIncrement()
    }

    ExperienceLink(nextIndex().some, `Akka`, `LiveSafe`) ::
      ExperienceLink(nextIndex().some, `Akka`, `Scala`) ::
      ExperienceLink(nextIndex().some, `Akka`, `Play Framework`) ::
      ExperienceLink(nextIndex().some, `Akka`, `Thompson-Reuters Special Services`) ::
      ExperienceLink(nextIndex().some, `Bootstrap`, `Thompson-Reuters Special Services`) ::
      ExperienceLink(nextIndex().some, `Cascading Style Sheets`, `Thompson-Reuters Special Services`) ::
      ExperienceLink(nextIndex().some, `Flyway`, `LiveSafe`) ::
      ExperienceLink(nextIndex().some, `Flyway`, `PostgreSQL`) ::
      ExperienceLink(nextIndex().some, `Lagom`, `LiveSafe`) ::
      ExperienceLink(nextIndex().some, `Lagom`, `Scala`) ::
      ExperienceLink(nextIndex().some, `Play Framework`, `LiveSafe`) ::
      ExperienceLink(nextIndex().some, `Play Framework`, `Thompson-Reuters Special Services`) ::
      ExperienceLink(nextIndex().some, `Play Framework`, `Verizon Business`) ::
      ExperienceLink(nextIndex().some, `PostgreSQL`, `LiveSafe`) ::
      ExperienceLink(nextIndex().some, `SBT`, `LiveSafe`) ::
      ExperienceLink(nextIndex().some, `SBT`, `Thompson-Reuters Special Services`) ::
      ExperienceLink(nextIndex().some, `SBT`, `Verizon Business`) ::
      ExperienceLink(nextIndex().some, `Scala`, `LiveSafe`) ::
      ExperienceLink(nextIndex().some, `Scala`, `Play Framework`) ::
      ExperienceLink(nextIndex().some, `Scala`, `SBT`) ::
      ExperienceLink(nextIndex().some, `Scala`, `Slick`) ::
      ExperienceLink(nextIndex().some, `Scala`, `Thompson-Reuters Special Services`) ::
      ExperienceLink(nextIndex().some, `Scala`, `Verizon Business`) ::
      ExperienceLink(nextIndex().some, `Slick`, `LiveSafe`) ::
      Nil
  }

  def apply(): HtmlElement = {

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
          experiences.nodes.map(node =>
            g(
              circle(
                r := "20",
                cx <-- node.$x.map(_.fold("")(_.toString)),
                cy <-- node.$y.map(_.fold("")(_.toString)),
                fill := (node match {
                  case _: ExperienceNode.Skill => "blue"
                  case _: ExperienceNode.Employment => "green"
                })
              ),
              text(
                x <-- node.$x.map(_.fold("")(_.toString)),
                y <-- node.$y.map(_.fold("")(_.toString)),
                style := "15px sans-serif",
                node.id)
            ))
        )
      )
    }

    val linkDistance = Var(10d)
    val linkStrength = Var(0.01d)
    val link: Link[ExperienceNode, ExperienceLink] =
      d3.forceLink[ExperienceNode, ExperienceLink](links.toJSArray)
        .distance(linkDistance.now())
        .strength(linkStrength.now())

    val chargeStrength = Var(-25d)
    val charge: ManyBody[ExperienceNode] =
      d3.forceManyBody()
        .strength(chargeStrength.now())

    val centeringX = Var(400)
    val centeringY = Var(300)
    val centering: Centering[ExperienceNode] =
      d3.forceCenter(centeringX.now(), centeringY.now())

    //val collisionStrength = Var(1d)
    //val collision: Collision[ExperienceNode] =
    //  d3.forceCollide()
    //    .strength(collisionStrength.now())
    //    .radius(_ => 30)

    val simulation =
      d3.forceSimulation(experiences.nodes.toJSArray)
        .force("link", link)
        .force("charge", charge)
        .force("center", centering)
    //.force("collide", collision)

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
        //div(
        //  className := "row",
        //  div(
        //    className := "col-12",
        //    span("Collision Strength: "),
        //    input(
        //      value <-- collisionStrength.signal.map(_.toString),
        //      inContext(el => onEnterPress.mapTo(el.ref.value).map(_.toDouble) --> collisionStrength.writer))
        //  )
        //),
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
      onMountCallback { context =>
        import context.owner
        linkDistance.signal.foreach(link.distance(_))
        linkDistance.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

        linkStrength.signal.foreach(link.strength(_))
        linkStrength.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

        chargeStrength.signal.foreach(charge.strength(_))
        chargeStrength.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

        //collisionStrength.signal.foreach(collision.strength(_))
        //collisionStrength.signal.mapToValue(1d).foreach(simulation.alphaTarget(_).restart())

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
