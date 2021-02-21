package ahlers.presence.web.client

import cats.syntax.option._
import ahlers.presence.web.client.UiState.{ FocusedResumePage, ResumePage, UnfocusedResumePage }
import ahlers.presence.web.client.resume._
import cats.syntax.apply._
import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ResumeView {

  def render($resumePage: Signal[ResumePage]): HtmlElement = {
    val onEnterPress = onKeyPress.filter(_.keyCode == KeyCode.Enter)

    val $focusedExperienceId: Signal[Option[ExperienceId]] =
      $resumePage.map {
        case UnfocusedResumePage => none
        case FocusedResumePage(experienceId) => experienceId.some
      }

    article(
      className := Seq("d-flex", "h-100"),
      ExperiencesGridView.render($focusedExperienceId),
      experiences.skills.map(ExperienceDetailSkillView.render(_, $focusedExperienceId)),
      experiences.employments.map(ExperienceDetailEmploymentView.render(_, $focusedExperienceId))
    )

  }

}
