package ahlers.presence.web.client

import cats.syntax.either._
import com.raquo.laminar.api.L._
import com.raquo.waypoint.{ endOfSegments, root, Route, Router }
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import org.scalajs.dom.window
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

/**
 * @since November 28, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
sealed trait UiState
object UiState extends LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")

  case object Landing extends UiState
  case object Resume extends UiState
  case object Contact extends UiState

  val landingRoute = Route.static(Landing, root / endOfSegments)
  val resumeRoute = Route.static(Resume, root / "resume" / endOfSegments)
  val contactRoute = Route.static(Contact, root / "contact" / endOfSegments)

  object router
    extends Router[UiState](
      initialUrl = window.location.href,
      origin = window.location.origin.get,
      routes = List(landingRoute, resumeRoute, contactRoute),
      owner = unsafeWindowOwner,
      $popStateEvent = windowEvents.onPopState,
      getPageTitle = _.toString,
      serializePage = _.asJson.noSpaces,
      deserializePage = decode[UiState](_).valueOr(throw _))

  //router
  //  .$currentPage
  //  .foreach(state =>
  //    logger
  //      .info(s"""Transitioned to state ${state.getClass()}."""))(unsafeWindowOwner)

}
