package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object FooterView {

  def apply(): HtmlElement =
    footer(
      className := "ui inverted vertical footer segment",
      div(
        className := "ui container",
        div(
          className := "ui stackable divided equal height stackable grid",
          div(
            className := "ui column seven wide list",
            div(
              className := "item",
              i(className := "envelope square icon"),
              a(className := "content", href := "mailto:michael@ahlers.consulting", "michael@ahlers.consulting")),
            div(
              className := "item",
              i(className := "linkedin square icon"),
              a(className := "content", href := "http://linkedin.com/in/michaelahlers", "/in/michaelahlers")),
            div(
              className := "item",
              i(className := "github square icon"),
              a(className := "content", href := "http://github.com/michaelahlers", "/michaelahlers")),
            div(
              className := "item",
              i(className := "stack overflow square icon"),
              a(className := "content", href := "http://stackoverflow.com/users/700420/michael-ahlers", "/users/700420/michael-ahlers")
            ),
            div(
              className := "item",
              i(className := "phone square icon"),
              a(className := "content", href := "tel:+1-571-830-0258", "+1 (571) 830-0258"))
          )
        )
      )
    )

}
