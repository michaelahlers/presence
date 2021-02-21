package ahlers.presence.web.client.resume

import cats.Eq
import cats.syntax.option._

import scala.language.postfixOps

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceId(toText: String) extends AnyVal

object ExperienceId {

  implicit val eqExperienceId: Eq[ExperienceId] =
    Eq.by(_.toText)

}

case class ExperienceName(toText: String) extends AnyVal

//case class ExperienceLinkUi(
//  source: ExperienceNodeUi,
//  target: ExperienceNodeUi)
//  extends SimulationLinkRx[ExperienceNodeUi, ExperienceNodeUi] {
//
//  /** For method chaining. */
//  // TODO: Factor out syntax for sim. node.
//  def withIndex(index: Option[Index]): ExperienceLinkUi = {
//    this.index = index.orUndefined
//    this
//  }
//
//  def withIndex(index: Index): ExperienceLinkUi =
//    withIndex(index.some)
//
//}

//object ExperienceLinkUi {
//
//  implicit class Syntax(private val self: ExperienceLinkUi) extends AnyVal {
//    import self.source
//    import self.target
//    def contains(node: ExperienceNodeUi): Boolean =
//      (source == node) ||
//        (target == node)
//  }
//
//}
