package ahlers.presence.web.client.resume

import com.raquo.airstream.signal.{ Signal, Var }
import d3v4.{ Index, SimulationNode }
import cats.syntax.option._

import scala.scalajs.js.{ constructorOf, UndefOr }
import scala.scalajs.js.JSConverters._

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait ExperienceNode extends SimulationNode {

  def id: String

  def indexVar: Var[Option[Index]]
  def xVar: Var[Option[Double]]
  def yVar: Var[Option[Double]]
  def vxVar: Var[Option[Double]]
  def vyVar: Var[Option[Double]]
  def fxVar: Var[Option[Double]]
  def fyVar: Var[Option[Double]]

  final val $index = indexVar.signal
  final override def index = indexVar.now().orUndefined
  final override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)

  final val $x = xVar.signal
  final override def x = xVar.now().orUndefined
  final override def x_=(x: UndefOr[Double]) = xVar.set(x.toOption)

  final val $y = yVar.signal
  final override def y = yVar.now().orUndefined
  final override def y_=(y: UndefOr[Double]) = yVar.set(y.toOption)

  final val $vx = vxVar.signal
  final override def vx = vxVar.now().orUndefined
  final override def vx_=(vx: UndefOr[Double]) = vxVar.set(vx.toOption)

  final val $vy = vyVar.signal
  final override def vy = vyVar.now().orUndefined
  final override def vy_=(vy: UndefOr[Double]) = vyVar.set(vy.toOption)

  final val $fx = fxVar.signal
  final override def fx = fxVar.now().orUndefined
  final override def fx_=(fx: UndefOr[Double]) = fxVar.set(fx.toOption)

  final val $fy = fyVar.signal
  final override def fy = fyVar.now().orUndefined
  final override def fy_=(fy: UndefOr[Double]) = fyVar.set(fy.toOption)

}

object ExperienceNode {

  case class Skill(
    id: String,
    name: String,
    indexVar: Var[Option[Index]],
    xVar: Var[Option[Double]],
    yVar: Var[Option[Double]],
    vxVar: Var[Option[Double]],
    vyVar: Var[Option[Double]],
    fxVar: Var[Option[Double]],
    fyVar: Var[Option[Double]])
    extends ExperienceNode

  object Skill {}

  def skill(
    id: String,
    name: String,
    index: Option[Index]
  ): Skill =
    Skill(
      id,
      name,
      Var(index),
      Var(none),
      Var(none),
      Var(none),
      Var(none),
      Var(none),
      Var(none))

  case class Employment(
    id: String,
    company: Employment.Company,
    indexVar: Var[Option[Index]],
    xVar: Var[Option[Double]],
    yVar: Var[Option[Double]],
    vxVar: Var[Option[Double]],
    vyVar: Var[Option[Double]],
    fxVar: Var[Option[Double]],
    fyVar: Var[Option[Double]])
    extends ExperienceNode

  object Employment {
    case class Company(
      name: String,
      city: String,
      state: String)
  }

  def employment(
    id: String,
    company: Employment.Company,
    index: Option[Index]
  ): Employment =
    Employment(
      id,
      company,
      Var(index),
      Var(none),
      Var(none),
      Var(none),
      Var(none),
      Var(none),
      Var(none))

}
