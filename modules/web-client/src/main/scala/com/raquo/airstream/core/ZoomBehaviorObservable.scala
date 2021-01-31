package com.raquo.airstream.core

import com.raquo.airstream.eventstream.EventStream
import d3v4.d3
import d3v4.d3.ZoomEvent
import d3v4.d3zoom.ZoomBehavior
import org.scalajs.dom
import org.scalajs.dom.EventTarget

import scala.util.Try

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
class ZoomBehaviorObservable(behavior: ZoomBehavior[dom.EventTarget]) extends EventStream[ZoomEvent] {

  override protected[airstream] val topoRank = 1

  override protected[this] def onStart() = {
    super.onStart()
    behavior.on("zoom", () => new Transaction(fireValue(d3.event, _)))
  }

  override protected[this] def onStop() = {
    super.onStop()
    behavior.on("zoom", null)
  }

}
