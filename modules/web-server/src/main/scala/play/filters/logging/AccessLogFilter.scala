package play.filters.logging

import akka.http.scaladsl.model.{ ContentType, HttpMethods, StatusCodes, Uri }
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

  import AccessLogFilter._

  override def apply(nextFilter: RequestHeader => Future[Result])(request: RequestHeader) =
    nextFilter(request)
      .andThen {
        case Success(result) => logSuccess(request, result)
        case Failure(reason) => logFailure(request, reason)
      }

}

object AccessLogFilter extends StrictLogging {

  val isInfo = logger.underlying.isInfoEnabled
  val isWarn = logger.underlying.isWarnEnabled
  val isError = logger.underlying.isErrorEnabled

  def logSuccess(request: RequestHeader, result: Result): Unit =
    if (isInfo || isWarn) {
      val method =
        HttpMethods
          .getForKey(request.method)

      val statusCode =
        StatusCodes
          .getForKey(result.header.status)

      val uri =
        Uri(request.host + request.uri)

      val contentType =
        result.body.contentType
          .flatMap(ContentType.parse(_)
            .toOption)

      val message = s"""${method.map(_.value).getOrElse("")}${statusCode.map(statusCode => s" ${statusCode.intValue} (${statusCode.reason})").getOrElse("")} ${uri}${contentType.map(" " + _).getOrElse("")}."""

      if (statusCode.exists(_.isFailure())) logger.warn(message)
      else logger.info(message)
    }

  def logFailure(request: RequestHeader, reason: Throwable): Unit =
    if (isError) {
      val method =
        HttpMethods
          .getForKey(request.method)

      val uri =
        Uri(request.host + request.uri)

      val message = s"""${method.map(_.value).getOrElse("")} ${uri}."""
      logger.error(message, reason)
    }

}
