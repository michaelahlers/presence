package ahlers.presence.web.server

import ahlers.presence.web.GetExperiencesResponse
import ahlers.presence.web.server.WebClientController.LoggingRequest
import akka.http.scaladsl.model.Uri.Path
import com.softwaremill.macwire._
import org.webjars.play.WebJarsUtil
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{ AbstractController, ControllerComponents }
import slogging.{ LoggerFactory, MessageLevel, StrictLogging }
import play.api.http.CirceWriteables._
import io.circe.syntax._

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 06, 2020
 */
class WebClientController(
  controllerComponents: ControllerComponents,
  webJars: WebJarsUtil)
  extends AbstractController(controllerComponents)
    with StrictLogging {

  def getExperiences() =
    Action { implicit request =>
      Ok(GetExperiencesResponse(
        records = ahlers.presence.experiences.all)
        .asJson)
    }

  def postLogs() =
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

  def getDefault(path: Path) =
    Action { implicit request =>
      Ok(wire[ahlers.presence.web.server.html.default].apply())
    }

}

object WebClientController {

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
