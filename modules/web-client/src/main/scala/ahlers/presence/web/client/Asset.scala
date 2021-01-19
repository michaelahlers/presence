package ahlers.presence.web.client

import io.lemonlabs.uri.{ Path, Url }

import scala.scalajs.js

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class Asset(url: Url, absoluteUrl: Url)
object Asset {

  def versioned(path: Path): Asset = {
    val request = js.Dynamic.global.jsRoutes.controllers.Assets.versioned(path.toString())
    Asset(
      Url.parse(request.url.toString()),
      Url.parse(request.absoluteURL().toString))
  }

  def versioned(path: String): Asset =
    versioned(Path.parse(path))

}
