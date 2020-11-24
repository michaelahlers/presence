package ahlers.presence.web.server

import play.api.mvc
import play.api.mvc.{ JavascriptLiteral, PathBindable }
import play.api.mvc.PathBindable.bindableString

/**
 * @since November 24, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object WebServerRoutesImport {

  type Path = akka.http.scaladsl.model.Uri.Path
  val Path = akka.http.scaladsl.model.Uri.Path

  implicit val pathBindablePath: PathBindable[Path] =
    implicitly[mvc.PathBindable[String]]
      .transform(Path(_), _.toString())

  implicit val javascriptLiteralPath: JavascriptLiteral[Path] =
    _.toString()

}
