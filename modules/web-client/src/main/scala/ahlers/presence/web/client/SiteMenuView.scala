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

  @js.native
  trait RawElement extends js.Object {
    def doThing(): Unit // Note: This is not actually implemented in mwc-button, just an example
  }

  type Ref = dom.html.Element //with RawElement

  def apply(modifiers: Modifier[ReactiveHtmlElement[Ref]]*): HtmlElement =
    nav(
      modifiers,
      div(
        className := "container",
        div(
          //className := "collapse navbar-collapse",
          className := "row",
          ul(
            className := "navbar-nav me-auto",
            SiteAnchorView(UiState.Landing, i(className := "fas fa-home")) //,
            //SiteAnchorView(UiState.Contact, span("Contact"))
          )
        )
      )
    )

}
