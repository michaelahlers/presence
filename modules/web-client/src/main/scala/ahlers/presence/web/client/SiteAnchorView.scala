package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object SiteAnchorView {

  def apply(uiState: UiState, label: HtmlElement): Li = {
    val $isActive: Signal[Boolean] =
      UiState.router
        .$currentPage
        .map(_ == uiState)

    li(
      className := "nav-item",
      className <-- $isActive.map(enabled => Map("active" -> enabled)),
      a(
        className := "nav-link",
        onClick.preventDefault.mapToValue(uiState) --> UiState.router.pushState _,
        href := UiState.router.relativeUrlForPage(uiState),
        label)
    )
  }

}
