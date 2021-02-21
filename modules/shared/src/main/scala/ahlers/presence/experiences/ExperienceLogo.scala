package ahlers.presence.experiences

import io.lemonlabs.uri.Uri

import scala.util.Try

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceLogo(toUri: Uri)
object ExperienceLogo {

  implicit class Syntax(private val self: ExperienceLogo) extends AnyVal {
    import self._
    def toText: String = toUri.toString()
  }

  implicit class CompanionSyntax(private val self: ExperienceLogo.type) extends AnyVal {
    def parseTry(fromText: String): Try[ExperienceLogo] = Uri.parseTry(fromText).map(ExperienceLogo(_))
  }

}
