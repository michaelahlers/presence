package ahlers.presence.web.client

import ahlers.presence.web.client.CssSettings._
import com.raquo.laminar.api.L._
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

  case class Asset(url: String, absoluteUrl: String)
  object Assets {
    def versioned(path: String): Asset = {
      val request = js.Dynamic.global.jsRoutes.controllers.Assets.versioned(path)
      Asset(request.url.toString, request.absoluteURL().toString)
    }

  }

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

  windowEvents
    .onLoad
    .foreach(_ => render(dom.document.body, SiteMenu()))(unsafeWindowOwner)

}
