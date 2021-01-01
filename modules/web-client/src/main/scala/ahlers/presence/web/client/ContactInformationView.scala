package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ContactInformationView {

  def apply(): HtmlElement = {
    val channelLabelAnchors: Seq[(String, HtmlElement, HtmlElement)] =
      ("mailto:michael@ahlers.consulting", i(className := "fas fa-envelope-square"), span(className := "ms-1", "michael@ahlers.consulting")) ::
        ("tel:+1-571-830-0258", i(className := "fas fa-phone-square"), span(className := "ms-1", "(571) 830-0258")) ::
        Nil

    val socialLabelAnchors: Seq[(String, HtmlElement, HtmlElement)] =
      ("http://linkedin.com/in/michaelahlers", i(className := "fab fa-linkedin"), span(className := "ms-1", "/in/michaelahlers")) ::
        ("http://github.com/michaelahlers", i(className := "fab fa-github-square"), span(className := "ms-1", "/michaelahlers")) ::
        ("http://stackexchange.com/users/359179/michael-ahlers", i(className := "fab fa-stack-exchange"), span(className := "ms-1", "/users/359179/michael-ahlers")) ::
        Nil

    div(
      className := "d-flex flex-row justify-content-center mt-3",
      div(
        className := "d-inline-flex",
        ul(
          className := "list-unstyled lead",
          channelLabelAnchors.map {
            case (reference, label, anchor) =>
              li(
                a(
                  className := "contact-link",
                  href := reference,
                  label,
                  anchor))
          }
        )
      ),
      div(
        className := "d-inline-flex ms-3",
        ul(
          className := "ps-3 list-unstyled lead border-start border-3 border-secondary",
          socialLabelAnchors.map {
            case (reference, label, anchor) =>
              li(
                a(
                  className := "contact-link",
                  href := reference,
                  label,
                  anchor))
          }
        )
      )
    )
  }

}
