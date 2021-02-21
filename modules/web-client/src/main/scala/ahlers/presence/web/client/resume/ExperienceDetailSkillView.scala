package ahlers.presence.web.client.resume

import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.UnfocusedResumePage
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._

/**
 * @since February 20, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceDetailSkillView {

  val onClickExitFocus: Modifier[Element] =
    onClick
      .stopPropagation
      .mapToValue(UnfocusedResumePage) --> (UiState.router.pushState(_))

  def render(
    skillDetail: ExperienceDetail.Skill,
    $focusedExperienceId: Signal[Option[ExperienceId]],
    modifiers: Modifier[Div]*
  ): Div = {
    val headerRender =
      div(
        className("modal-header"),
        h1(
          className("modal-title"),
          skillDetail.id.toText),
        button(
          tpe("button"),
          className("btn-close"),
          onClickExitFocus))

    val bodyRender =
      div(
        className("modal-body"),
        p(skillDetail.name.toText))

    val footerRender =
      div(
        className("modal-footer"),
        button(
          tpe("button"),
          className("btn", "btn-secondary"),
          onClickExitFocus,
          "Close"))

    val $isRaised: Signal[Boolean] =
      $focusedExperienceId.map(_.contains(skillDetail.id))

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
      $focusedExperienceId.map(_.filter(_ == skillDetail.id)) --> (x => UiState.modalsVar.update(_.filterNot(_ == skillDetail.id.toText) ++ x.map(_.toText)))
    )
  }

}
