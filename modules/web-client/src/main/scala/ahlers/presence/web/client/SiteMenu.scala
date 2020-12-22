package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object SiteMenu {

  def apply(): Div =
    div(
      className := "ui large top fixed inverted pointing menu",
      div(
        className := GlobalStyles.semantic.container.htmlClass,
        SiteLink(UiState.Landing, i(className := "home icon")),
        SiteLink(UiState.Resume, span("Resume")),
        SiteLink(UiState.Contact, span("Contact"))
      )
    )

}
