package d3.laminar.syntax

import _root_.d3.laminar.syntax.ZoomSyntax._
import com.raquo.airstream.core.Observer
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveElement
import d3v4.d3
import d3v4.d3.ZoomBehavior
import d3v4.d3selection.Selection
import d3v4.d3zoom.ZoomEvent
import org.scalajs.dom

import scala.language.implicitConversions

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait ZoomSyntax {

  implicit final def implyD3LaminarSyntaxZoomBehaviorEventTarget(behavior: ZoomBehavior[dom.EventTarget]): ZoomBehaviorEventTargetOps =
    new ZoomBehaviorEventTargetOps(behavior)

}

object ZoomSyntax {

  class ZoomBehaviorEventTargetOps(private val behavior: ZoomBehavior[dom.EventTarget]) extends AnyVal {

    @inline def -->(observer: Observer[ZoomEvent]): Binder[Element] =
      ReactiveElement.bindSubscription(_) { context =>
        val selection: Selection[dom.EventTarget] = d3.select(context.thisNode.ref)

        /** @todo Overload [[d3v4.d3zoom.ZoomBehavior.on]] with [[https://github.com/d3/d3-selection#selection_on listener function type taking documented event, datum, and target]]. */
        behavior
          .on("zoom", () => observer.onNext(d3.event))
          .apply(selection)

        new Subscription(
          context.owner,
          cleanup = () =>
            behavior
              .on("zoom", null)
              .apply(selection))
      }

    // And so on…

    //@inline def -->(onNext: ZoomEvent => Unit): Binder[ReactiveElement.Base] =
    //  -->(Observer(onNext))

    //@inline def -->(eventBus: EventBus[ZoomEvent]): Binder[ReactiveElement.Base] =
    //  -->(eventBus.writer)

    //@inline def -->(targetVar: Var[ZoomEvent]): Binder[ReactiveElement.Base] =
    //  -->(targetVar.writer)

  }

}
