package ahlers.presence.web.client

import cats.syntax.semigroup._
import cats.instances.map._
import cats.instances.set._
import cats.syntax.option._
import com.raquo.airstream.signal.Var

/**
 * @since January 11, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
package object resume {

  object experiences {

    val Akka = ExperienceBrief.skill(ExperienceId("akka"), ExperienceName("Akka"), "https://seeklogo.com/images/A/akka-logo-24316F492F-seeklogo.com.png")
    val Bootstrap = ExperienceBrief.skill(
      ExperienceId("bootstrap"),
      ExperienceName("Bootstrap"),
      "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Bootstrap_logo.svg/1024px-Bootstrap_logo.svg.png"
    )
    val CSS = ExperienceBrief.skill(ExperienceId("css"), ExperienceName("CSS"), "https://cdn.worldvectorlogo.com/logos/css3.svg")
    val Flyway = ExperienceBrief.skill(ExperienceId("flyway"), ExperienceName("Flyway"))
    val SBT = ExperienceBrief.skill(ExperienceId("sbt"), ExperienceName("SBT"))
    val Scala = ExperienceBrief.skill(ExperienceId("scala"), ExperienceName("Scala"), "https://www.scala-lang.org/resources/img/frontpage/scala-spiral.png")
    val Slick = ExperienceBrief.skill(ExperienceId("slick"), ExperienceName("Slick"))
    val Lagom = ExperienceBrief.skill(ExperienceId("lagom"), ExperienceName("Lagom"), "https://avatars3.githubusercontent.com/u/17168851?s=280&v=4")
    val PlayFramework = ExperienceBrief.skill(
      ExperienceId("play-framework"),
      ExperienceName("Play Framework"),
      "https://www.playframework.com/assets/images/logos/1d627942f0b2f115f8638936a212244a-play_icon_full_color.png"
    )
    val PostgreSQL = ExperienceBrief.skill(
      ExperienceId("postgresql"),
      ExperienceName("PostgreSQL"),
      "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Postgresql_elephant.svg/1200px-Postgresql_elephant.svg.png"
    )

    val LiveSafe =
      ExperienceBrief.employment(
        ExperienceId("livesafe"),
        ExperienceBrief.Employment.Company(
          "LiveSafe",
          "Rosslyn",
          "Virginia"),
        "https://media.glassdoor.com/sqll/968163/livesafe-squarelogo-1498735659534.png"
      )

    val ThompsonReutersSpecialServices =
      ExperienceBrief.employment(
        ExperienceId("trss"),
        ExperienceBrief.Employment.Company(
          "Thompson-Reuters Special Services",
          "McLean",
          "Virginia"))

    val VerizonBusiness =
      ExperienceBrief.employment(
        ExperienceId("verizon-business"),
        ExperienceBrief.Employment.Company(
          "Verizon Business",
          "Ashburn",
          "Virginia"))

    val descriptions: Seq[ExperienceBrief] =
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

    val nodes: Seq[ExperienceNodeUi] =
      (descriptions ++ Seq.fill(1000)(ExperienceBrief.Blank))
        .zipWithIndex
        .map { case (detail, index) =>
          ExperienceNodeUi(index, detail)
        }

    val relationSets: Seq[Set[_ <: ExperienceBrief]] =
      Set(Akka, Lagom, PlayFramework, Scala, Slick) ::
        Set(Bootstrap, CSS) ::
        Set(Flyway, PostgreSQL, Slick) ::
        Set(LiveSafe, Akka, Lagom, SBT, Scala) ::
        Set(ThompsonReutersSpecialServices, Bootstrap, CSS, PlayFramework, SBT, Scala) ::
        Set(VerizonBusiness, Bootstrap, CSS, PlayFramework, SBT, Scala) ::
        Nil

    //val links: Seq[ExperienceLinkUi] = {
    //  val byId: Map[ExperienceId, ExperienceNodeUi] =
    //    nodes
    //      .groupBy(_.experience.id)
    //      .view.mapValues(_.head)
    //      .toMap
    //
    //  (for {
    //    rs <- relationSets
    //    a <- rs
    //    b <- rs
    //    if a != b
    //  } yield (a, b))
    //    .groupBy { case (a, b) => Set(a, b) }
    //    .values
    //    .zipWithIndex
    //    .map { case (Seq((a, b), _ @_*), index) =>
    //      ExperienceLinkUi(byId(a.id), byId(b.id))
    //        .withIndex(index)
    //    }
    //    .toSeq
    //}

    //val adjacentLinks: Map[ExperienceNodeUi, Set[ExperienceLinkUi]] =
    //  links
    //    .foldLeft(Map.empty[ExperienceNodeUi, Set[ExperienceLinkUi]]) { case (a, link) =>
    //      a |+| Map(
    //        (link.source, Set(link)),
    //        (link.target, Set(link)))
    //    }
    //    .withDefaultValue(Set.empty)

    //val adjacentNodes: Map[ExperienceNodeUi, Set[ExperienceNodeUi]] =
    //  links
    //    .foldLeft(Map.empty[ExperienceNodeUi, Set[ExperienceNodeUi]]) { case (a, link) =>
    //      a |+| Map(
    //        (link.source, Set(link.target)),
    //        (link.target, Set(link.source)))
    //    }
    //    .withDefaultValue(Set.empty)

  }

}
