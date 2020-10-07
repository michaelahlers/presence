package ahlers.presence.web.server

import play.api.mvc.{ AbstractController, ControllerComponents }

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 06, 2020
 */
class WebUiController(
  controllerComponents: ControllerComponents)
  extends AbstractController(controllerComponents) {

  def index =
    Action {
      Ok(ahlers.presence.web.server.html.index())
    }
}
