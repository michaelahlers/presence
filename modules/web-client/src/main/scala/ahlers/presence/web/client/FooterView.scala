package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object FooterView {

  def apply(): HtmlElement =
    footer(
      className("footer", "mt-auto", "bg-dark"),
      div(
        className("container-fluid"),
        ContactInformationView()))

}
