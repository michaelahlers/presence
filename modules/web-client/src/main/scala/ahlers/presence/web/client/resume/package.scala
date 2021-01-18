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

    val nodes: Seq[ExperienceNodeUi] =
      (descriptions ++ Seq.fill(1000)(ExperienceDescription.Blank))
        .zipWithIndex
        .map { case (detail, index) =>
          ExperienceNodeUi(index, detail)
        }

    val relationSets: Seq[Set[_ <: ExperienceDescription]] =
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
