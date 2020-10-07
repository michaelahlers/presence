package ahlers.presence.web.server

import play.api.mvc.{ AbstractController, ControllerComponents }
import slogging.StrictLogging

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 06, 2020
 */
class WebUiController(
  controllerComponents: ControllerComponents)
  extends AbstractController(controllerComponents)
    with StrictLogging {

  def logging =
    Action { request =>
      println(request)
      Ok
    }

  def index =
    Action { request =>
      logger.info(s"$request")
      Ok(ahlers.presence.web.server.html.index())
    }
}
