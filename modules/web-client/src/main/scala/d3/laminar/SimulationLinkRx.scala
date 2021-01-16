package d3.laminar

import com.raquo.airstream.signal.Var
import d3v4.{ Index, SimulationLink, SimulationNode }

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait SimulationLinkRx[Source <: SimulationNode, Target <: SimulationNode] extends SimulationLink[Source, Target] {
  def indexVar: Var[Option[Index]]
  def sourceVar: Var[Source]
  def targetVar: Var[Target]

  final def $index = indexVar.signal
  final override def index = indexVar.now().orUndefined
  final override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)

  final def $source = sourceVar.signal
  final override def source = sourceVar.now()

  final def $target = targetVar.signal
  final override def target = targetVar.now()

}

object SimulationLinkRx {

  //def apply[Source <: SimulationNode, Target <: SimulationNode](
  //  index: Option[Index],
  //  source: Source,
  //  target: Target
  //): SimulationLinkRx[Source, Target] =
  //  SimulationLinkRx(
  //    Var(index),
  //    Var(source),
  //    Var(target))
  //
  //def apply[Source <: SimulationNode, Target <: SimulationNode](
  //  index: Index,
  //  source: Source,
  //  target: Target
  //): SimulationLinkRx[Source, Target] =
  //  SimulationLinkRx(
  //    index.some,
  //    source,
  //    target)

}
