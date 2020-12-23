package ahlers.presence.web.client

import ahlers.presence.web.client.CssSettings._
import com.raquo.laminar.api.L._
import com.raquo.waypoint.SplitRender
import org.scalajs.dom
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

import scala.scalajs.js

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebClientApplication extends App with LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")
  GlobalStyles.addToDocument()

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

  val pageSplitter =
    SplitRender[UiState, HtmlElement](UiState.router.$currentPage)
      .collectStatic(UiState.Landing)(LandingPageView())
      .collectStatic(UiState.Resume)(ResumePage())
      .collectStatic(UiState.Contact)(ContactPage())

  val site: Div =
    div(
      SiteMenu(),
      child <-- pageSplitter.$view)

  documentEvents
    .onDomContentLoaded
    .foreach(_ => render(dom.document.body, site))(unsafeWindowOwner)

}
