package d3.laminar

import d3v4.{ Index, SimulationLink, SimulationNode }

import scala.scalajs.js

/**
 * @since January 03, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait SimulationLinkRx[Source <: SimulationNode, Target <: SimulationNode]
  extends SimulationLink[
    Source,
    Target
  ] {

  // FIXME: Disabled pending Airstream fix.
  //final val indexVar: Var[Option[Index]] = Var(none)
  //final val $index: StrictSignal[Option[Index]] = indexVar.signal
  //final override def index = indexVar.now().orUndefined
  //final override def index_=(index: UndefOr[Index]) = indexVar.set(index.toOption)
  final var index: js.UndefOr[Index] = js.undefined

}
