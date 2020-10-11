package ahlers.presence.web.server

import ahlers.presence.web.server.WebUiController.LoggingRequest
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.{ AbstractController, ControllerComponents }
import slogging.{ LoggerFactory, MessageLevel, StrictLogging }

import scala.util.control.NoStackTrace

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 06, 2020
 */
class WebUiController(
  controllerComponents: ControllerComponents)
  extends AbstractController(controllerComponents)
    with StrictLogging {

  def logging =
    Action(parse.json[LoggingRequest]) { request =>
      import MessageLevel._
      import request.body

      val logger = LoggerFactory.getLogger(body.name)

      body.level match {
        case `trace` => logger.trace(body.message)
        case `debug` => logger.debug(body.message)
        case `info` => logger.info(body.message)
        case `warn` => logger.warn(body.message)
        case `error` => logger.error(body.message)
      }

      Ok
    }

  def index =
    Action { implicit request =>
      logger.info(s"$request")
      Ok(ahlers.presence.web.server.html.index())
    }
}

object WebUiController {

  case class LoggingRequest(
    level: MessageLevel,
    name: String,
    message: String)

  object LoggingRequest {

    implicit val readsMessageLevel: Reads[MessageLevel] =
      Reads
        .of[String](Reads.pattern("(trace|debug|info|warn|error)".r))
        .map {
          case "trace" => MessageLevel.trace
          case "debug" => MessageLevel.debug
          case "info" => MessageLevel.info
          case "warn" => MessageLevel.warn
          case "error" => MessageLevel.error
        }

    implicit val readsLoggingRequest: Reads[LoggingRequest] =
      (__ \ "level").read[MessageLevel]
        .and((__ \ "name").read[String])
        .and((__ \ "msg").read[String])
        .apply(LoggingRequest(_, _, _))

  }

}
