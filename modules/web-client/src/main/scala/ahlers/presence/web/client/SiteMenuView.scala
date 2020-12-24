package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object SiteMenuView {

  def apply(): Div =
    div(
      className := "ui large top fixed inverted pointing menu",
      div(
        className := "ui container",
        SiteAnchorView(UiState.Landing, i(className := "home icon")),
        //SiteLink(UiState.Resume, span("Resume")),
        SiteAnchorView(UiState.Contact, span("Contact"))
      )
    )

}
