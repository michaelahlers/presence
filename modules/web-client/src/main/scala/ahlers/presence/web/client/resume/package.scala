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

    val Akka =
      ExperienceBrief.skill(
        ExperienceId("akka"),
        ExperienceName("Akka"),
        Asset.versioned("/ahlers/presence/web/experiences/akka-brief.svg").absoluteUrl.toString())

    val Bootstrap =
      ExperienceBrief.skill(
        ExperienceId("bootstrap"),
        ExperienceName("Bootstrap"),
        Asset.versioned("/ahlers/presence/web/experiences/bootstrap-brief.svg").absoluteUrl.toString())

    val CSS =
      ExperienceBrief.skill(
        ExperienceId("css"),
        ExperienceName("CSS"),
        Asset.versioned("/ahlers/presence/web/experiences/css3-brief.svg").absoluteUrl.toString())

    val D3 =
      ExperienceBrief.skill(
        ExperienceId("d3"),
        ExperienceName("D3"),
        Asset.versioned("/ahlers/presence/web/experiences/d3-brief.svg").absoluteUrl.toString())

    val Flyway =
      ExperienceBrief.skill(
        ExperienceId("flyway"),
        ExperienceName("Flyway"),
        Asset.versioned("/ahlers/presence/web/experiences/flyway-brief.svg").absoluteUrl.toString())

    val HTML =
      ExperienceBrief.skill(
        ExperienceId("html"),
        ExperienceName("HTML"),
        Asset.versioned("/ahlers/presence/web/experiences/html5-brief.svg").absoluteUrl.toString())

    val Java =
      ExperienceBrief.skill(
        ExperienceId("java"),
        ExperienceName("Java"),
        Asset.versioned("/ahlers/presence/web/experiences/java-brief.svg").absoluteUrl.toString())

    val JavaScript =
      ExperienceBrief.skill(
        ExperienceId("javascript"),
        ExperienceName("JavaScript"),
        Asset.versioned("/ahlers/presence/web/experiences/javascript-brief.svg").absoluteUrl.toString()
      )

    val SBT =
      ExperienceBrief.skill(
        ExperienceId("sbt"),
        ExperienceName("SBT"),
        Asset.versioned("/ahlers/presence/web/experiences/sbt-brief.svg").absoluteUrl.toString())

    val Scala =
      ExperienceBrief.skill(
        ExperienceId("scala"),
        ExperienceName("Scala"),
        Asset.versioned("/ahlers/presence/web/experiences/scala-brief.svg").absoluteUrl.toString())

    val Slick =
      ExperienceBrief.skill(
        ExperienceId("slick"),
        ExperienceName("Slick"),
        Asset.versioned("/ahlers/presence/web/experiences/slick-brief.svg").absoluteUrl.toString())

    val Lagom =
      ExperienceBrief.skill(
        ExperienceId("lagom"),
        ExperienceName("Lagom"),
        Asset.versioned("/ahlers/presence/web/experiences/lagom-brief.svg").absoluteUrl.toString())

    val Linux =
      ExperienceBrief.skill(
        ExperienceId("linux"),
        ExperienceName("Linux"),
        Asset.versioned("/ahlers/presence/web/experiences/linux-brief.svg").absoluteUrl.toString())

    val PlayFramework = ExperienceBrief.skill(
      ExperienceId("play-framework"),
      ExperienceName("Play Framework"),
      Asset.versioned("/ahlers/presence/web/experiences/play-framework-brief.svg").absoluteUrl.toString()
    )

    val PostgreSQL = ExperienceBrief.skill(
      ExperienceId("postgresql"),
      ExperienceName("PostgreSQL"),
      Asset.versioned("/ahlers/presence/web/experiences/postgresql-brief.svg").absoluteUrl.toString()
    )

    val LiveSafe =
      ExperienceBrief.employment(
        ExperienceId("livesafe"),
        ExperienceBrief.Employment.Company(
          "LiveSafe",
          "Rosslyn",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/livesafe-brief.svg").absoluteUrl.toString()
      )

    val ThompsonReutersSpecialServices =
      ExperienceBrief.employment(
        ExperienceId("trss"),
        ExperienceBrief.Employment.Company(
          "Thompson-Reuters Special Services",
          "McLean",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/trss-brief.svg").absoluteUrl.toString()
      )

    val VerizonBusiness =
      ExperienceBrief.employment(
        ExperienceId("verizon-business"),
        ExperienceBrief.Employment.Company(
          "Verizon Business",
          "Ashburn",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/verizon-brief.svg").absoluteUrl.toString()
      )

    val descriptions: Seq[ExperienceBrief] =
      Akka ::
        Bootstrap ::
        CSS ::
        HTML ::
        Java ::
        JavaScript ::
        Flyway ::
        Lagom ::
        Linux ::
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
