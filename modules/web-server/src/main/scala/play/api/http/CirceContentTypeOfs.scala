package play.api.http

import io.circe.Json
import play.api.mvc.Codec

/**
 * @since February 21, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object CirceContentTypeOfs {

  implicit def contentTypeOfCirceJson(implicit codec: Codec): ContentTypeOf[Json] =
    ContentTypeOf[Json](Some(ContentTypes.JSON))

}
