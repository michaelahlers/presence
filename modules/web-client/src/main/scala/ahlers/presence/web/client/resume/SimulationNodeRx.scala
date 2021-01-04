package ahlers.presence.web.client.resume

import cats.syntax.option._
import com.raquo.airstream.signal.{ Signal, Var }
import d3v4.{ Index, SimulationNode }

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{ constructorOf, UndefOr }

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class SimulationNodeRx[Payload](
  indexVar: Var[Option[Index]],
  xVar: Var[Option[Double]],
  yVar: Var[Option[Double]],
  vxVar: Var[Option[Double]],
  vyVar: Var[Option[Double]],
  fxVar: Var[Option[Double]],
  fyVar: Var[Option[Double]],
  payloadVar: Var[Payload])
  extends SimulationNode {

  val $index = indexVar.signal
  override def index = indexVar.now().orUndefined
  override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)

  val $x = xVar.signal
  override def x = xVar.now().orUndefined
  override def x_=(x: UndefOr[Double]) = xVar.set(x.toOption)

  val $y = yVar.signal
  override def y = yVar.now().orUndefined
  override def y_=(y: UndefOr[Double]) = yVar.set(y.toOption)

  val $vx = vxVar.signal
  override def vx = vxVar.now().orUndefined
  override def vx_=(vx: UndefOr[Double]) = vxVar.set(vx.toOption)

  val $vy = vyVar.signal
  override def vy = vyVar.now().orUndefined
  override def vy_=(vy: UndefOr[Double]) = vyVar.set(vy.toOption)

  val $fx = fxVar.signal
  override def fx = fxVar.now().orUndefined
  override def fx_=(fx: UndefOr[Double]) = fxVar.set(fx.toOption)

  val $fy = fyVar.signal
  override def fy = fyVar.now().orUndefined
  override def fy_=(fy: UndefOr[Double]) = fyVar.set(fy.toOption)

  val $payload: Signal[Payload] = payloadVar.signal
  def payload: Payload = payloadVar.now()

}

object SimulationNodeRx {

  def apply[Payload](
    index: Option[Index],
    payload: Payload
  ): SimulationNodeRx[Payload] =
    SimulationNodeRx(
      Var(index),
      Var(none),
      Var(none),
      Var(none),
      Var(none),
      Var(none),
      Var(none),
      Var(payload))

  def apply[Payload](
    index: Index,
    payload: Payload
  ): SimulationNodeRx[Payload] =
    SimulationNodeRx(
      index.some,
      payload)

}
