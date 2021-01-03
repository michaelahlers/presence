package ahlers.presence.web.client.resume

import cats.syntax.option._
import com.raquo.airstream.signal.Var
import d3v4.{ Index, SimulationLink }

import scala.scalajs.js.UndefOr
import scala.scalajs.js.JSConverters._

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceLink extends SimulationLink[ExperienceNode, ExperienceNode] {

  def indexVar: Var[Option[Index]]

  final val $index = indexVar.signal
  final override def index = indexVar.now().orUndefined
  final override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)

}

object ExperienceLink {

  case class Default(
    indexVar: Var[Option[Index]],
    source: ExperienceNode,
    target: ExperienceNode)
    extends ExperienceLink

  def apply(
    index: Option[Index],
    source: ExperienceNode,
    target: ExperienceNode
  ): ExperienceLink =
    Default(
      Var(index),
      source,
      target)

}
