package play.filters.logging

import akka.http.scaladsl.model.StatusCode
import akka.stream.Materializer
import play.api.mvc.{ Filter, RequestHeader, Result }
import slogging.StrictLogging

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

/**
 * @since November 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
class AccessLogFilter(
  implicit
  executionContext: ExecutionContext,
  override val mat: Materializer)
  extends Filter
    with StrictLogging {

  override def apply(nextFilter: RequestHeader => Future[Result])(request: RequestHeader) =
    nextFilter(request)
      .andThen {

        case Success(result) if StatusCode.int2StatusCode(result.header.status).isFailure() =>
          logger.warn(s"""${request.method} ${request.uri} ${result.header.status}${result.body.contentType.map(" " + _).getOrElse("")}.""")

        case Success(result) =>
          logger.info(s"""${request.method} ${request.uri} ${result.header.status}${result.body.contentType.map(" " + _).getOrElse("")}.""")

        case Failure(reason) =>
          logger.error(s"""${request.method} ${request.uri}.""", reason)

      }
}
