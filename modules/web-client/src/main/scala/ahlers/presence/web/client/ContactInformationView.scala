package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ContactInformationView {

  def apply(): HtmlElement = {
    val referenceLabelAnchors: Seq[(String, HtmlElement, HtmlElement)] =
      ("mailto:michael@ahlers.consulting", i(className := "fas fa-envelope-square"), span("michael@ahlers.consulting")) ::
        ("http://linkedin.com/in/michaelahlers", i(className := "fab fa-linkedin"), span("/in/michaelahlers")) ::
        ("http://github.com/michaelahlers", i(className := "fab fa-github-square"), span("/michaelahlers")) ::
        ("http://stackexchange.com/users/359179/michael-ahlers", i(className := "fab fa-stack-exchange"), span("/users/359179/michael-ahlers")) ::
        ("tel:+1-571-830-0258", i(className := "fas fa-phone-square"), span("+1 (571) 830-0258")) ::
        Nil

    dl(
      className := "list-unstyled",
      referenceLabelAnchors.map {
        case (reference, label, anchor) =>
          a(
            className := "row",
            href := reference,
            dt(className := "col-1", label),
            dd(className := "col-11 mb-0 ps-1", anchor))
      }
    )
  }

}
