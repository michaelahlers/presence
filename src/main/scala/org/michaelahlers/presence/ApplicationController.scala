package org.michaelahlers.presence

import javax.inject.Inject

import com.typesafe.scalalogging.LazyLogging
import play.api.mvc.{Action, Controller}

class ApplicationController @Inject()
  extends Controller
          with LazyLogging {

  def index = Action {
    Ok(org.michaelahlers.presence.html.index())
  }
}
