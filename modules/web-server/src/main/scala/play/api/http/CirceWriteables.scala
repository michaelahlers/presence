package play.api.http

import akka.util.ByteString
import io.circe.Json
import play.api.http.CirceContentTypeOfs._

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object CirceWriteables {

  implicit def writableOfCirceJson: Writeable[Json] =
    Writeable(entity => ByteString.fromString(entity.spaces2))

}
