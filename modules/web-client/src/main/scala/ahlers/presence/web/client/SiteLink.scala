package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object SiteLink {

  def apply(uiState: UiState, label: HtmlElement): Anchor = {
    val $isActive: Signal[Boolean] =
      UiState.router
        .$currentPage
        .map(_ == uiState)

    a(
      className := "item",
      className <-- $isActive.map(enabled => Map("active" -> enabled)),
      onClick.preventDefault.mapToValue(uiState) --> UiState.router.pushState _,
      href := UiState.router.relativeUrlForPage(uiState),
      label
    )
  }

}
