package ahlers.presence.web.client

import scala.scalajs.js

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class Asset(url: String, absoluteUrl: String)
object Asset {
  def versioned(path: String): Asset = {
    val request = js.Dynamic.global.jsRoutes.controllers.Assets.versioned(path)
    Asset(request.url.toString, request.absoluteURL().toString)
  }
}
