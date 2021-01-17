package d3.laminar

import com.raquo.airstream.signal.Var
import typings.d3Force.mod.{ SimulationLinkDatum, SimulationNodeDatum }

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait SimulationLinkRx[NodeDatum <: SimulationNodeDatum] extends SimulationLinkDatum[NodeDatum] {

  def indexVar: Var[Option[Double]]
  def sourceVar: Var[NodeDatum]
  def targetVar: Var[NodeDatum]

  final def $index = indexVar.signal
  final override var index = indexVar.now().orUndefined
  final def index_=(index: UndefOr[Double]) = indexVar.set(index.toOption)

  final def $source = sourceVar.signal
  final override var source = sourceVar.now()
  final def source_=(source: NodeDatum) = sourceVar.set(source)

  final def $target = targetVar.signal
  final override var target = targetVar.now()
  final def target_=(target: NodeDatum) = targetVar.set(target)

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
