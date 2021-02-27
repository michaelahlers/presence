package ahlers.presence.web.client

import ahlers.presence.experiences.ExperienceKey
import cats.syntax.either._
import com.raquo.laminar.api.L._
import com.raquo.waypoint._
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

  sealed trait ResumePage extends UiState
  case object UnfocusedResumePage extends ResumePage
  case class FocusedResumePage(experienceId: ExperienceKey) extends ResumePage

  case object Contact extends UiState

  val landingRoute = Route.static(Landing, root / endOfSegments)

  val resumeRoute = Route.static(UnfocusedResumePage, root / "resume" / endOfSegments)

  val resumeFocusRoute: Route[FocusedResumePage, String] =
    Route(
      encode = _.experienceId.toText,
      decode = fromText => FocusedResumePage(ExperienceKey(fromText)),
      root / "resume" / segment[String] / endOfSegments)

  val contactRoute = Route.static(Contact, root / "contact" / endOfSegments)

  object router
    extends Router[UiState](
      routes = List(landingRoute, resumeRoute, resumeFocusRoute, contactRoute),
      getPageTitle = _.toString,
      serializePage = _.asJson.noSpaces,
      deserializePage = decode[UiState](_).valueOr(throw _))(
      initialUrl = window.location.href,
      origin = window.location.origin.get,
      owner = unsafeWindowOwner,
      $popStateEvent = windowEvents.onPopState)

  val modalsVar: Var[Set[String]] = Var(Set.empty)
  val $modals: Signal[Set[String]] = modalsVar.signal

  //router
  //  .$currentPage
  //  .foreach(state =>
  //    logger
  //      .info(s"""Transitioned to state ${state.getClass()}."""))(unsafeWindowOwner)

}
