package ahlers.presence.web.client.resume

import cats.syntax.option._
import com.raquo.airstream.signal.Var
import d3v4.{ Index, SimulationLink, SimulationNode }

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class SimulationLinkRx[Source <: SimulationNode, Target <: SimulationNode](
  indexVar: Var[Option[Index]],
  sourceVar: Var[Source],
  targetVar: Var[Target])
  extends SimulationLink[Source, Target] {

  final val $index = indexVar.signal
  final override def index = indexVar.now().orUndefined
  final override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)

  final val $source = sourceVar.signal
  final override def source = sourceVar.now()

  final val $target = targetVar.signal
  final override def target = targetVar.now()

}

object SimulationLinkRx {

  def apply[Source <: SimulationNode, Target <: SimulationNode](
    index: Option[Index],
    source: Source,
    target: Target
  ): SimulationLinkRx[Source, Target] =
    SimulationLinkRx(
      Var(index),
      Var(source),
      Var(target))

  def apply[Source <: SimulationNode, Target <: SimulationNode](
    index: Index,
    source: Source,
    target: Target
  ): SimulationLinkRx[Source, Target] =
    SimulationLinkRx(
      index.some,
      source,
      target)

}
