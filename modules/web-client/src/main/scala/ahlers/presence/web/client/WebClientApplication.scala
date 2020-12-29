package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import org.scalajs.dom
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebClientApplication extends App with LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")

  //GlobalStyles.addToDocument()
  //GlobalRegistry.addToDocumentOnRegistration()

  //$(".masthead")
  //  .visibility(SemanticUiVisibilitySettings
  //    .once(false)
  //    .onBottomPassed(() =>
  //      $(".fixed.menu")
  //        .transition("fade in"))
  //    .onBottomPassedReverse(() =>
  //      $(".fixed.menu")
  //        .transition("fade out")))
  //
  //$(".ui.sidebar")
  //  .sidebar("attach events", ".toc.item")

  /** Bootstrap utility classes can't be assigned to parents using modifiers. */
  Seq("h-100")
    .foreach(dom
      .document
      .body
      .parentElement
      .classList
      .add(_))

  Seq("d-flex", "flex-column", "h-100")
    .foreach(dom
      .document
      .body
      .classList
      .add(_))

  documentEvents
    .onDomContentLoaded
    .mapToValue(Seq(
      HeaderView(),
      MainView(),
      FooterView()))
    .foreach(_.foreach(
      render(dom.document.body, _)))(unsafeWindowOwner)

}
