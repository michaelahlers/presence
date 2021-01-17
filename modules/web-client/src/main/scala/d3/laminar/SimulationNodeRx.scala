package d3.laminar

import com.raquo.airstream.signal.{ Signal, Var }
import typings.d3Force.mod.SimulationNodeDatum

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait SimulationNodeRx[Payload] extends SimulationNodeDatum {

  def indexVar: Var[Option[Double]]
  def xVar: Var[Option[Double]]
  def yVar: Var[Option[Double]]
  def vxVar: Var[Option[Double]]
  def vyVar: Var[Option[Double]]
  def fxVar: Var[Option[Double]]
  def fyVar: Var[Option[Double]]
  def payloadVar: Var[Payload]

  final def $index = indexVar.signal
  final def index = indexVar.now().orUndefined
  final def index_=(index: UndefOr[Double]) = indexVar.set(index.toOption)

  final def $x = xVar.signal
  final override var x = xVar.now().orUndefined
  final def x_=(x: UndefOr[Double]) = xVar.set(x.toOption)

  final def $y = yVar.signal
  final override var y = yVar.now().orUndefined
  final def y_=(y: UndefOr[Double]) = yVar.set(y.toOption)

  final def $vx = vxVar.signal
  final override var vx = vxVar.now().orUndefined
  final def vx_=(vx: UndefOr[Double]) = vxVar.set(vx.toOption)

  final def $vy = vyVar.signal
  final override var vy = vyVar.now().orUndefined
  final def vy_=(vy: UndefOr[Double]) = vyVar.set(vy.toOption)

  final def $fx = fxVar.signal
  final override var fx = fxVar.now().orUndefined
  final def fx_=(fx: UndefOr[Double]) = fxVar.set(fx.toOption)

  final def $fy = fyVar.signal
  final override var fy = fyVar.now().orUndefined
  final def fy_=(fy: UndefOr[Double]) = fyVar.set(fy.toOption)

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
