package d3.laminar

import cats.syntax.option._
import com.raquo.airstream.signal.{ StrictSignal, Var }
import d3v4.{ Index, SimulationNode }

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait SimulationNodeRx extends SimulationNode {

  // FIXME: Disabled pending Airstream fix.
  //final val indexVar: Var[Option[Index]] = Var(none)
  //final val $index: StrictSignal[Option[Index]] = indexVar.signal
  //final override def index = indexVar.now().orUndefined
  //final override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)
  final var index: js.UndefOr[Index] = js.undefined

  final val xVar: Var[Option[Double]] = Var(none)
  final val $x: StrictSignal[Option[Double]] = xVar.signal
  final override def x = xVar.now().orUndefined
  final override def x_=(x: UndefOr[Double]) = xVar.set(x.toOption)

  final val yVar: Var[Option[Double]] = Var(none)
  final val $y: StrictSignal[Option[Double]] = yVar.signal
  final override def y = yVar.now().orUndefined
  final override def y_=(y: UndefOr[Double]) = yVar.set(y.toOption)

  final val vxVar: Var[Option[Double]] = Var(none)
  final val $vx: StrictSignal[Option[Double]] = vxVar.signal
  final override def vx = vxVar.now().orUndefined
  final override def vx_=(vx: UndefOr[Double]) = vxVar.set(vx.toOption)

  final val vyVar: Var[Option[Double]] = Var(none)
  final val $vy: StrictSignal[Option[Double]] = vyVar.signal
  final override def vy = vyVar.now().orUndefined
  final override def vy_=(vy: UndefOr[Double]) = vyVar.set(vy.toOption)

  final val fxVar: Var[Option[Double]] = Var(none)
  final val $fx: StrictSignal[Option[Double]] = fxVar.signal
  final override def fx = fxVar.now().orUndefined
  final override def fx_=(fx: UndefOr[Double]) = fxVar.set(fx.toOption)

  final val fyVar: Var[Option[Double]] = Var(none)
  final val $fy: StrictSignal[Option[Double]] = fyVar.signal
  final override def fy = fyVar.now().orUndefined
  final override def fy_=(fy: UndefOr[Double]) = fyVar.set(fy.toOption)

}
