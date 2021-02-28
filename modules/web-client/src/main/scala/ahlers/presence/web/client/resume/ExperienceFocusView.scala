package ahlers.presence.web.client.resume

import ahlers.presence.experiences.{ Experience, ExperienceKey }
import cats.syntax.option._
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._

/**
 * @since February 20, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceFocusView {

  val onClickClose =
    onClick
      .stopPropagation
      .mapToStrict(none)

  def render(
    experienceKey: ExperienceKey,
    experience: Experience,
    $experience: Signal[Experience],
    $focusedExperienceKey: Signal[Option[ExperienceKey]],
    focusedExperienceObserver: Observer[Option[ExperienceKey]]
  ): Div = {
    val headerRender =
      div(
        className("modal-header"),
        h1(
          className("modal-title"),
          child.text <-- $experience.map(_.brief.name.toText)),
        button(
          tpe("button"),
          className("btn-close"),
          onClickClose --> focusedExperienceObserver)
      )

    val bodyRender =
      div(
        className("modal-body"),
        p(child.text <-- $experience.map(_.detail.name.toText)))

    val footerRender =
      div(
        className("modal-footer"),
        button(
          tpe("button"),
          className("btn", "btn-secondary"),
          onClickClose --> focusedExperienceObserver,
          "Close"))

    val $isRaised: Signal[Boolean] =
      $focusedExperienceKey.combineWith($experience.map(_.key))
        .mapN(_.contains(_))

    div(
      className("modal", "fade", "d-block"),
      className.toggle("show") <-- $isRaised,
      tabIndex(-1),
      div(
        className("modal-dialog", "modal-dialog-centered", "modal-dialog-scrollable"),
        div(
          className("modal-content"),
          headerRender,
          bodyRender,
          footerRender
        ))
    )
  }

}
