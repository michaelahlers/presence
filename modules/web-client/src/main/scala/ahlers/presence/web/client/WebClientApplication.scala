package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.window
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebClientApplication extends LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")

  sealed trait Page
  case object HomePage extends Page
  case object WorkPage extends Page
  case object CompanyPage extends Page
  case object CareersPage extends Page

  val router = {
    import com.raquo.waypoint._

    val homeRoute = Route.static(HomePage, root / "home" / endOfSegments)
    val workRoute = Route.static(WorkPage, root / "work" / endOfSegments)
    val companyRoute = Route.static(CompanyPage, root / "company" / endOfSegments)
    val careersRoute = Route.static(CareersPage, root / "careers" / endOfSegments)

    new Router[Page](
      initialUrl = window.location.href,
      origin = window.location.origin.get,
      routes = List(homeRoute, workRoute, companyRoute, careersRoute),
      owner = unsafeWindowOwner,
      $popStateEvent = windowEvents.onPopState,
      getPageTitle = _.toString,
      serializePage = {
        case HomePage => "home"
        case WorkPage => "work"
        case CompanyPage => "company"
        case CareersPage => "careers"
      },
      deserializePage = {
        case "home" => HomePage
        case "work" => WorkPage
        case "company" => CompanyPage
        case "careers" => CareersPage
      }
    )
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
      def link(page: Page, label: String) =
        a(
          className := "item",
          onClick.preventDefault.mapToValue(page) --> (router.pushState(_)),
          href := router.relativeUrlForPage(page),
          label)

      div(
        className := "ui inverted vertical masthead center aligned segment",
        div(
          className := "ui container",
          div(
            className := "ui large secondary inverted pointing menu",
            link(HomePage, "Home"),
            link(WorkPage, "Work"),
            link(CompanyPage, "Company"),
            link(CareersPage, "Careers"),
            div(
              className := "right item",
              a(
                className := "ui inverted button",
                "Log In"),
              a(
                className := "ui inverted button",
                "Sign Up")
            )
          )
        )
      )
    }

    windowEvents
      .onLoad
      .foreach(_ => render(dom.document.body, menu))(unsafeWindowOwner)
  }

}
