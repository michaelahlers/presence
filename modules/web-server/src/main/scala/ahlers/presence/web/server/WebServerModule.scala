package ahlers.presence.web.server

import java.time.Clock

import com.softwaremill.macwire._
import controllers.AssetsComponents
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.filters.HttpFiltersComponents
import router.Routes

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 06, 2020
 */
class WebServerModule(
  context: Context)
  extends BuiltInComponentsFromContext(context)
    with AssetsComponents
    with HttpFiltersComponents {

  implicit val clock = Clock.systemUTC()

  override val httpFilters =
    super.httpFilters

  val uiController = wire[WebUiController]

  val router: Routes = {
    val prefix: String = "/"
    wire[Routes]
  }

  //AkkaManagement(actorSystem).start()

}
