package d3.laminar

import com.raquo.airstream.signal.{ Signal, Var }
import d3v4.{ Index, SimulationNode }

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait SimulationNodeRx[Payload] extends SimulationNode {
  def indexVar: Var[Option[Index]]
  def xVar: Var[Option[Double]]
  def yVar: Var[Option[Double]]
  def vxVar: Var[Option[Double]]
  def vyVar: Var[Option[Double]]
  def fxVar: Var[Option[Double]]
  def fyVar: Var[Option[Double]]
  def payloadVar: Var[Payload]

  final def $index = indexVar.signal
  final override def index = indexVar.now().orUndefined
  final override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)

  final def $x = xVar.signal
  final override def x = xVar.now().orUndefined
  final override def x_=(x: UndefOr[Double]) = xVar.set(x.toOption)

  final def $y = yVar.signal
  final override def y = yVar.now().orUndefined
  final override def y_=(y: UndefOr[Double]) = yVar.set(y.toOption)

  final def $vx = vxVar.signal
  final override def vx = vxVar.now().orUndefined
  final override def vx_=(vx: UndefOr[Double]) = vxVar.set(vx.toOption)

  final def $vy = vyVar.signal
  final override def vy = vyVar.now().orUndefined
  final override def vy_=(vy: UndefOr[Double]) = vyVar.set(vy.toOption)

  final def $fx = fxVar.signal
  final override def fx = fxVar.now().orUndefined
  final override def fx_=(fx: UndefOr[Double]) = fxVar.set(fx.toOption)

  final def $fy = fyVar.signal
  final override def fy = fyVar.now().orUndefined
  final override def fy_=(fy: UndefOr[Double]) = fyVar.set(fy.toOption)

  final def $payload: Signal[Payload] = payloadVar.signal
  final def payload: Payload = payloadVar.now()

}

object SimulationNodeRx {

  //def apply[Payload](
  //  index: Option[Index],
  //  payload: Payload
  //): SimulationNodeRx[Payload] =
  //  SimulationNodeRx(
  //    Var(index),
  //    Var(none),
  //    Var(none),
  //    Var(none),
  //    Var(none),
  //    Var(none),
  //    Var(none),
  //    Var(payload))
  //
  //def apply[Payload](
  //  index: Index,
  //  payload: Payload
  //): SimulationNodeRx[Payload] =
  //  SimulationNodeRx(
  //    index.some,
  //    payload)

}
