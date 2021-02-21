package ahlers.presence.web.client

import ahlers.presence.web.client.resume.ExperienceDetail.{ Employment, Skill }
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
      ExperienceDetail.Skill(
        ExperienceId("akka"),
        ExperienceName("Akka"),
        Asset.versioned("/ahlers/presence/web/experiences/akka-brief.svg").absoluteUrl.toString())

    val Bootstrap =
      ExperienceDetail.Skill(
        ExperienceId("bootstrap"),
        ExperienceName("Bootstrap"),
        Asset.versioned("/ahlers/presence/web/experiences/bootstrap-brief.svg").absoluteUrl.toString())

    val CSS =
      ExperienceDetail.Skill(
        ExperienceId("css"),
        ExperienceName("CSS"),
        Asset.versioned("/ahlers/presence/web/experiences/css3-brief.svg").absoluteUrl.toString())

    val D3 =
      ExperienceDetail.Skill(
        ExperienceId("d3"),
        ExperienceName("D3"),
        Asset.versioned("/ahlers/presence/web/experiences/d3-brief.svg").absoluteUrl.toString())

    val DataGrip =
      ExperienceDetail.Skill(
        ExperienceId("datagrip"),
        ExperienceName("DataGrip"),
        Asset.versioned("/ahlers/presence/web/experiences/datagrip-brief.svg").absoluteUrl.toString())

    val Flyway =
      ExperienceDetail.Skill(
        ExperienceId("flyway"),
        ExperienceName("Flyway"),
        Asset.versioned("/ahlers/presence/web/experiences/flyway-brief.svg").absoluteUrl.toString())

    val Git =
      ExperienceDetail.Skill(
        ExperienceId("git"),
        ExperienceName("Git"),
        Asset.versioned("/ahlers/presence/web/experiences/git-brief.svg").absoluteUrl.toString())

    val GitHub =
      ExperienceDetail.Skill(
        ExperienceId("github"),
        ExperienceName("GitHub"),
        Asset.versioned("/ahlers/presence/web/experiences/github-brief.svg").absoluteUrl.toString())

    val IntelliJ =
      ExperienceDetail.Skill(
        ExperienceId("intellij-idea"),
        ExperienceName("IntelliJ IDEA"),
        Asset.versioned("/ahlers/presence/web/experiences/intellij-idea-brief.svg").absoluteUrl.toString()
      )

    val HTML =
      ExperienceDetail.Skill(
        ExperienceId("html"),
        ExperienceName("HTML"),
        Asset.versioned("/ahlers/presence/web/experiences/html5-brief.svg").absoluteUrl.toString())

    val Java =
      ExperienceDetail.Skill(
        ExperienceId("java"),
        ExperienceName("Java"),
        Asset.versioned("/ahlers/presence/web/experiences/java-brief.svg").absoluteUrl.toString())

    val JavaScript =
      ExperienceDetail.Skill(
        ExperienceId("javascript"),
        ExperienceName("JavaScript"),
        Asset.versioned("/ahlers/presence/web/experiences/javascript-brief.svg").absoluteUrl.toString()
      )

    val jQuery =
      ExperienceDetail.Skill(
        ExperienceId("jquery"),
        ExperienceName("jQuery"),
        Asset.versioned("/ahlers/presence/web/experiences/jquery-brief.svg").absoluteUrl.toString())

    val Kafka =
      ExperienceDetail.Skill(
        ExperienceId("kafka"),
        ExperienceName("Kafka"),
        Asset.versioned("/ahlers/presence/web/experiences/kafka-brief.svg").absoluteUrl.toString())

    val SBT =
      ExperienceDetail.Skill(
        ExperienceId("sbt"),
        ExperienceName("SBT"),
        Asset.versioned("/ahlers/presence/web/experiences/sbt-brief.svg").absoluteUrl.toString())

    val Scala =
      ExperienceDetail.Skill(
        ExperienceId("scala"),
        ExperienceName("Scala"),
        Asset.versioned("/ahlers/presence/web/experiences/scala-brief.svg").absoluteUrl.toString())

    val ScalaJs =
      ExperienceDetail.Skill(
        ExperienceId("scala.js"),
        ExperienceName("Scala.js"),
        Asset.versioned("/ahlers/presence/web/experiences/scala.js-brief.svg").absoluteUrl.toString())

    val Slick =
      ExperienceDetail.Skill(
        ExperienceId("slick"),
        ExperienceName("Slick"),
        Asset.versioned("/ahlers/presence/web/experiences/slick-brief.svg").absoluteUrl.toString())

    val Lagom =
      ExperienceDetail.Skill(
        ExperienceId("lagom"),
        ExperienceName("Lagom"),
        Asset.versioned("/ahlers/presence/web/experiences/lagom-brief.svg").absoluteUrl.toString())

    val Linux =
      ExperienceDetail.Skill(
        ExperienceId("linux"),
        ExperienceName("Linux"),
        Asset.versioned("/ahlers/presence/web/experiences/linux-brief.svg").absoluteUrl.toString())

    val MySQL =
      ExperienceDetail.Skill(
        ExperienceId("mysql"),
        ExperienceName("MySQL"),
        Asset.versioned("/ahlers/presence/web/experiences/mysql-brief.svg").absoluteUrl.toString())

    val MongoDB =
      ExperienceDetail.Skill(
        ExperienceId("mongodb"),
        ExperienceName("mongodb"),
        Asset.versioned("/ahlers/presence/web/experiences/mongodb-brief.svg").absoluteUrl.toString())

    val PlayFramework = ExperienceDetail.Skill(
      ExperienceId("play-framework"),
      ExperienceName("Play Framework"),
      Asset.versioned("/ahlers/presence/web/experiences/play-framework-brief.svg").absoluteUrl.toString()
    )

    val PostgreSQL = ExperienceDetail.Skill(
      ExperienceId("postgresql"),
      ExperienceName("PostgreSQL"),
      Asset.versioned("/ahlers/presence/web/experiences/postgresql-brief.svg").absoluteUrl.toString()
    )

    val LiveSafe =
      ExperienceDetail.Employment(
        ExperienceId("livesafe"),
        ExperienceDetail.Employment.Company(
          "LiveSafe",
          none,
          "Rosslyn",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/livesafe-brief.svg").absoluteUrl.toString()
      )

    val ThompsonReutersSpecialServices =
      ExperienceDetail.Employment(
        ExperienceId("trss"),
        ExperienceDetail.Employment.Company(
          "Thompson-Reuters Special Services",
          "TRSS".some,
          "McLean",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/trss-brief.svg").absoluteUrl.toString()
      )

    val VerizonBusiness =
      ExperienceDetail.Employment(
        ExperienceId("verizon-business"),
        ExperienceDetail.Employment.Company(
          "Verizon Business",
          "Verizon".some,
          "Ashburn",
          "Virginia"),
        Asset.versioned("/ahlers/presence/web/experiences/verizon-brief.svg").absoluteUrl.toString()
      )

    val descriptions: Seq[ExperienceDetail] =
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

    val skills: Seq[Skill] =
      descriptions.collect { case skill: Skill => skill }

    val employments: Seq[Employment] =
      descriptions.collect { case employment: Employment => employment }

    val relationSets: Seq[Set[_ <: ExperienceDetail]] =
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
