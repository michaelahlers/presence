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
      ExperienceBrief.Skill(
        ExperienceId("akka"),
        ExperienceName("Akka"),
        Asset.versioned("/ahlers/presence/web/experiences/akka-brief.svg").absoluteUrl.toString())

    val Bootstrap =
      ExperienceBrief.Skill(
        ExperienceId("bootstrap"),
        ExperienceName("Bootstrap"),
        Asset.versioned("/ahlers/presence/web/experiences/bootstrap-brief.svg").absoluteUrl.toString())

    val CSS =
      ExperienceBrief.Skill(
        ExperienceId("css"),
        ExperienceName("CSS"),
        Asset.versioned("/ahlers/presence/web/experiences/css3-brief.svg").absoluteUrl.toString())

    val D3 =
      ExperienceBrief.Skill(
        ExperienceId("d3"),
        ExperienceName("D3"),
        Asset.versioned("/ahlers/presence/web/experiences/d3-brief.svg").absoluteUrl.toString())

    val DataGrip =
      ExperienceBrief.Skill(
        ExperienceId("datagrip"),
        ExperienceName("DataGrip"),
        Asset.versioned("/ahlers/presence/web/experiences/datagrip-brief.svg").absoluteUrl.toString())

    val Flyway =
      ExperienceBrief.Skill(
        ExperienceId("flyway"),
        ExperienceName("Flyway"),
        Asset.versioned("/ahlers/presence/web/experiences/flyway-brief.svg").absoluteUrl.toString())

    val Git =
      ExperienceBrief.Skill(
        ExperienceId("git"),
        ExperienceName("Git"),
        Asset.versioned("/ahlers/presence/web/experiences/git-brief.svg").absoluteUrl.toString())

    val GitHub =
      ExperienceBrief.Skill(
        ExperienceId("github"),
        ExperienceName("GitHub"),
        Asset.versioned("/ahlers/presence/web/experiences/github-brief.svg").absoluteUrl.toString())

    val IntelliJ =
      ExperienceBrief.Skill(
        ExperienceId("intellij-idea"),
        ExperienceName("IntelliJ IDEA"),
        Asset.versioned("/ahlers/presence/web/experiences/intellij-idea-brief.svg").absoluteUrl.toString()
      )

    val HTML =
      ExperienceBrief.Skill(
        ExperienceId("html"),
        ExperienceName("HTML"),
        Asset.versioned("/ahlers/presence/web/experiences/html5-brief.svg").absoluteUrl.toString())

    val Java =
      ExperienceBrief.Skill(
        ExperienceId("java"),
        ExperienceName("Java"),
        Asset.versioned("/ahlers/presence/web/experiences/java-brief.svg").absoluteUrl.toString())

    val JavaScript =
      ExperienceBrief.Skill(
        ExperienceId("javascript"),
        ExperienceName("JavaScript"),
        Asset.versioned("/ahlers/presence/web/experiences/javascript-brief.svg").absoluteUrl.toString()
      )

    val jQuery =
      ExperienceBrief.Skill(
        ExperienceId("jquery"),
        ExperienceName("jQuery"),
        Asset.versioned("/ahlers/presence/web/experiences/jquery-brief.svg").absoluteUrl.toString())

    val Kafka =
      ExperienceBrief.Skill(
        ExperienceId("kafka"),
        ExperienceName("Kafka"),
        Asset.versioned("/ahlers/presence/web/experiences/kafka-brief.svg").absoluteUrl.toString())

    val SBT =
      ExperienceBrief.Skill(
        ExperienceId("sbt"),
        ExperienceName("SBT"),
        Asset.versioned("/ahlers/presence/web/experiences/sbt-brief.svg").absoluteUrl.toString())

    val Scala =
      ExperienceBrief.Skill(
        ExperienceId("scala"),
        ExperienceName("Scala"),
        Asset.versioned("/ahlers/presence/web/experiences/scala-brief.svg").absoluteUrl.toString())

    val ScalaJs =
      ExperienceBrief.Skill(
        ExperienceId("scala.js"),
        ExperienceName("Scala.js"),
        Asset.versioned("/ahlers/presence/web/experiences/scala.js-brief.svg").absoluteUrl.toString())

    val Slick =
      ExperienceBrief.Skill(
        ExperienceId("slick"),
        ExperienceName("Slick"),
        Asset.versioned("/ahlers/presence/web/experiences/slick-brief.svg").absoluteUrl.toString())

    val Lagom =
      ExperienceBrief.Skill(
        ExperienceId("lagom"),
        ExperienceName("Lagom"),
        Asset.versioned("/ahlers/presence/web/experiences/lagom-brief.svg").absoluteUrl.toString())

    val Linux =
      ExperienceBrief.Skill(
        ExperienceId("linux"),
        ExperienceName("Linux"),
        Asset.versioned("/ahlers/presence/web/experiences/linux-brief.svg").absoluteUrl.toString())

    val MySQL =
      ExperienceBrief.Skill(
        ExperienceId("mysql"),
        ExperienceName("MySQL"),
        Asset.versioned("/ahlers/presence/web/experiences/mysql-brief.svg").absoluteUrl.toString())

    val MongoDB =
      ExperienceBrief.Skill(
        ExperienceId("mongodb"),
        ExperienceName("mongodb"),
        Asset.versioned("/ahlers/presence/web/experiences/mongodb-brief.svg").absoluteUrl.toString())

    val PlayFramework = ExperienceBrief.Skill(
      ExperienceId("play-framework"),
      ExperienceName("Play Framework"),
      Asset.versioned("/ahlers/presence/web/experiences/play-framework-brief.svg").absoluteUrl.toString()
    )

    val PostgreSQL = ExperienceBrief.Skill(
      ExperienceId("postgresql"),
      ExperienceName("PostgreSQL"),
      Asset.versioned("/ahlers/presence/web/experiences/postgresql-brief.svg").absoluteUrl.toString()
    )

    val LiveSafe =
      ExperienceBrief.Employment(
        ExperienceId("livesafe"),
        ExperienceBrief.Employment.Company(
          "LiveSafe",
          none,
          "Rosslyn",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/livesafe-brief.svg").absoluteUrl.toString()
      )

    val ThompsonReutersSpecialServices =
      ExperienceBrief.Employment(
        ExperienceId("trss"),
        ExperienceBrief.Employment.Company(
          "Thompson-Reuters Special Services",
          "TRSS".some,
          "McLean",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/trss-brief.svg").absoluteUrl.toString()
      )

    val VerizonBusiness =
      ExperienceBrief.Employment(
        ExperienceId("verizon-business"),
        ExperienceBrief.Employment.Company(
          "Verizon Business",
          "Verizon".some,
          "Ashburn",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/verizon-brief.svg").absoluteUrl.toString()
      )

    val descriptions: Seq[ExperienceBrief] =
      Akka ::
        Bootstrap ::
        CSS ::
        D3 ::
        DataGrip ::
        Flyway ::
        Git ::
        GitHub ::
        HTML ::
        IntelliJ ::
        Java ::
        JavaScript ::
        jQuery ::
        Kafka ::
        Lagom ::
        Linux ::
        MySQL ::
        MongoDB ::
        PlayFramework ::
        PostgreSQL ::
        SBT ::
        Scala ::
        ScalaJs ::
        Slick ::
        LiveSafe ::
        ThompsonReutersSpecialServices ::
        VerizonBusiness ::
        Nil

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
