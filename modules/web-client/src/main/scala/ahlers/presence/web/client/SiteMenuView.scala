package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object SiteMenuView {

  def apply(): HtmlElement =
    nav(
      className := "navbar navbar-expand-lg navbar-dark fixed-top bg-dark",
      div(
        className := "container",
        div(
          className := "collapse navbar-collapse",
          ul(
            className := "navbar-nav me-auto mb-2 mb-lg-0",
            SiteAnchorView(UiState.Landing, i(className := "fas fa-home")),
            SiteAnchorView(UiState.Contact, span("Contact"))
          )
        )
      )
    )

}
