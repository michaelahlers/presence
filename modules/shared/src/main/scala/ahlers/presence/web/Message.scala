package ahlers.presence.web

import play.api.libs.json._

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 06, 2020
 */
case class Message(text: String)
object Message {
  implicit val formatMessage: Format[Message] = Json.format
}
