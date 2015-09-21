package ahlers.michael.presence.server.api

import play.api.libs.json.Json._
import play.api.mvc.{Controller, Action}

class ApplicationController extends Controller {

  def getSessions = Action {
    Ok(arr(
      obj("user" -> "foo0"),
      obj("user" -> "bear0")
    ))
  }

}
