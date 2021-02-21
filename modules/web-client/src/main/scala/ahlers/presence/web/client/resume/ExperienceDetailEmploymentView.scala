package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.UnfocusedResumePage
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._

/**
 * @since February 20, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceDetailEmploymentView {

  val onClickExitFocus: Modifier[Element] =
    onClick
      .stopPropagation
      .mapToValue(UnfocusedResumePage) --> (UiState.router.pushState(_))

  def render(
    employmentDetail: ExperienceDetail.Employment,
    $focusedExperienceId: Signal[Option[ExperienceId]],
    modifiers: Modifier[Div]*
  ): Div = {
    val headerRender =
      div(
        className("modal-header"),
        h1(
          className("modal-title"),
          employmentDetail.id.toText),
        button(
          tpe("button"),
          className("btn-close"),
          onClickExitFocus))

    val bodyRender =
      div(
        className("modal-body"),
        p(employmentDetail.company.name))

    val footerRender =
      div(
        className("modal-footer"),
        button(
          tpe("button"),
          className("btn", "btn-secondary"),
          onClickExitFocus,
          "Close"))

    val $isRaised: Signal[Boolean] =
      $focusedExperienceId.map(_.contains(employmentDetail.id))

    div(
      className("modal", "fade", "d-block"),
      className.toggle("show") <-- $isRaised,
      tabIndex := -1,
      div(
        className("modal-dialog", "modal-fullscreen"),
        div(
          className("modal-content"),
          headerRender,
          bodyRender,
          footerRender
        )),
      $focusedExperienceId.map(_.filter(_ == employmentDetail.id)) --> (x => UiState.modalsVar.update(_.filterNot(_ == employmentDetail.id.toText) ++ x.map(_.toText)))
    )
  }

}
