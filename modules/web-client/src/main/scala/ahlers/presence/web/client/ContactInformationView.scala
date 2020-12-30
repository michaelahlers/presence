package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ContactInformationView {

  def apply(): HtmlElement = {
    val referenceLabelAnchors: Seq[(String, HtmlElement, HtmlElement)] =
      ("mailto:michael@ahlers.consulting", i(className := "fas fa-envelope-square"), span(className := "ms-2", "michael@ahlers.consulting")) ::
        ("http://linkedin.com/in/michaelahlers", i(className := "fab fa-linkedin"), span(className := "ms-2", "/in/michaelahlers")) ::
        ("http://github.com/michaelahlers", i(className := "fab fa-github-square"), span(className := "ms-2", "/michaelahlers")) ::
        ("http://stackexchange.com/users/359179/michael-ahlers", i(className := "fab fa-stack-exchange"), span(className := "ms-2", "/users/359179/michael-ahlers")) ::
        ("tel:+1-571-830-0258", i(className := "fas fa-phone-square"), span(className := "ms-2", "+1 (571) 830-0258")) ::
        Nil

    ul(
      className := "list-unstyled lead",
      referenceLabelAnchors.map {
        case (reference, label, anchor) =>
          li(
            a(
              href := reference,
              label,
              anchor))
      }
    )
  }

}
