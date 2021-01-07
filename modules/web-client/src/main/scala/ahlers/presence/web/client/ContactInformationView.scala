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
        ("tel:+1-571-279-9825", i(className := "fas fa-phone-square"), span(className := "ms-1", "(571) 279-9825")) ::
        Nil

    val socialLabelAnchors: Seq[(String, HtmlElement, HtmlElement)] =
      ("http://linkedin.com/in/michaelahlers", i(className := "fab fa-linkedin"), span(className := "ms-1", "LinkedIn")) ::
        ("http://github.com/michaelahlers", i(className := "fab fa-github-square"), span(className := "ms-1", "GitHub")) ::
        ("http://stackexchange.com/users/359179/michael-ahlers", i(className := "fab fa-stack-exchange"), span(className := "ms-1", "Stack Exchange")) ::
        Nil

    div(
      className := "row d-flex justify-content-center mt-3",
      div(
        className := "col-sm-6 col-12 d-flex justify-content-sm-end",
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
        className := "col-sm-5 col-12",
        ul(
          // FIXME: Restore border but disabled at extra-small.
          //className := "ps-3 list-unstyled lead border-start border-3 border-secondary",
          className := "list-unstyled lead",
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
