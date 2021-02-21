package ahlers.presence.web.client

import ahlers.presence.experiences.{ Experience, ExperienceKey }
import ahlers.presence.web.GetExperiencesResponse
import cats.syntax.option._
import cats.syntax.either._
import cats.instances.option._
import ahlers.presence.web.client.UiState.{ FocusedResumePage, ResumePage, UnfocusedResumePage }
import ahlers.presence.web.client.resume._
import cats.syntax.apply._
import com.raquo.airstream.signal.Signal
import io.circe.scalajs._
import io.circe.syntax._
import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.experimental._
import org.scalajs.dom.ext.KeyCode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumeView {

  def render($resumePage: Signal[ResumePage]): HtmlElement = {
    val $experiences: Signal[Option[Seq[Experience]]] =
      Signal.fromFuture(
        Fetch
          .fetch("/api/v1/experiences").toFuture
          .flatMap(_.json().toFuture)
          .map(decodeJs[GetExperiencesResponse](_).toTry)
          .flatMap(Future.fromTry(_))
          .map(_.records))

    val $focusedExperienceKey: Signal[Option[ExperienceKey]] =
      $resumePage
        .map {
          case UnfocusedResumePage => none
          case FocusedResumePage(key) => key.some
        }

    val focusedExperienceKeyBus: EventBus[Option[ExperienceKey]] = new EventBus()

    val onFocusedExperience: Binder[Element] =
      focusedExperienceKeyBus.events
        .map {
          case None => UnfocusedResumePage
          case Some(experienceKey) => FocusedResumePage(experienceKey)
        } --> (UiState.router.pushState(_))

    val focusedExperienceKeyObserver =
      focusedExperienceKeyBus.writer

    article(
      className := Seq("d-flex", "h-100"),
      onFocusedExperience,
      ExperiencesGridView.render($experiences, $focusedExperienceKey, focusedExperienceKeyObserver),
      children <--
        $experiences
          .map(_.getOrElse(Nil))
          .split(_.key)(ExperienceFocusView.render(_, _, _, $focusedExperienceKey, focusedExperienceKeyObserver))
    )

  }

}
