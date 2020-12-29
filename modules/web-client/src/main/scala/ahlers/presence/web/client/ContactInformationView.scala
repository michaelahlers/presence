package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ContactInformationView {

  def contactLabelAnchors: Seq[(HtmlElement, HtmlElement)] =
    (i(className := "fas fa-envelope-square"), a(href := "mailto:michael@ahlers.consulting", "michael@ahlers.consulting")) ::
      (i(className := "fab fa-linkedin"), a(href := "http://linkedin.com/in/michaelahlers", "/in/michaelahlers")) ::
      (i(className := "fab fa-github-square"), a(href := "http://github.com/michaelahlers", "/michaelahlers")) ::
      (i(className := "fab fa-stack-exchange"), a(href := "http://stackexchange.com/users/359179/michael-ahlers", "/users/359179/michael-ahlers")) ::
      (i(className := "fas fa-phone-square"), a(href := "tel:+1-571-830-0258", "+1 (571) 830-0258")) ::
      Nil

  // FIXME: Replace table with semantic tags.
  def apply(): HtmlElement =
    table(tbody(
      contactLabelAnchors.map { case (label, anchor) =>
        tr(
          th(className := "pe-1", label),
          td(anchor))
      }))

}
