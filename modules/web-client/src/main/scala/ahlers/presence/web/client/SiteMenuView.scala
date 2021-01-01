package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object SiteMenuView {

  type Ref = dom.html.Element

  def apply(modifiers: Modifier[ReactiveHtmlElement[Ref]]*): HtmlElement =
    nav(
      modifiers,
      div(
        className := "container-fluid",
        ul(
          className := "navbar-nav me-auto",
          SiteAnchorView(UiState.Landing, i(className := "fas fa-home")))))

}
