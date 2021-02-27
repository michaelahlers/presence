package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object SiteAnchorView {

  type Ref = dom.html.Element

  def apply(uiState: UiState, modifiers: Modifier[ReactiveHtmlElement[Ref]]*): Li = {
    val $isActive: Signal[Boolean] =
      UiState.router
        .$currentPage
        .map(_ == uiState)

    li(
      className := "nav-item",
      className <-- $isActive.map(enabled => Map("active" -> enabled)),
      a(
        className := "nav-link",
        onClick.preventDefault.mapToStrict(uiState) --> (UiState.router.pushState(_)),
        href := UiState.router.relativeUrlForPage(uiState),
        modifiers
      )
    )
  }

}
