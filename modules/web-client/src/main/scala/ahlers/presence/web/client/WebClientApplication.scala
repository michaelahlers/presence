package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import org.scalajs.dom
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

import scala.scalajs.js

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebClientApplication extends LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")

  case class Asset(url: String, absoluteUrl: String)
  object Assets {
    def versioned(path: String): Asset = {
      val request = js.Dynamic.global.jsRoutes.controllers.Assets.versioned(path)
      Asset(request.url.toString, request.absoluteURL().toString)
    }

  }

  def main(arguments: Array[String]): Unit = {

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

    def menu = {
      println(Assets.versioned("ahlers/presence/web/server/default.css"))

      def link(page: UiState, label: HtmlElement) =
        a(
          className := "item",
          onClick.preventDefault.mapToValue(page) --> (UiState.router.pushState(_)),
          href := UiState.router.relativeUrlForPage(page),
          label)

      div(
        className := "ui inverted vertical masthead center aligned segment",
        div(
          className := "ui container",
          div(
            className := "ui large secondary inverted pointing menu",
            link(UiState.Landing, i(className := "home icon")),
            link(UiState.Resume, span("Resume")),
            link(UiState.Contact, span("Contact"))
          )
        )
      )
    }

    windowEvents
      .onLoad
      .foreach(_ => render(dom.document.body, menu))(unsafeWindowOwner)
  }

}
