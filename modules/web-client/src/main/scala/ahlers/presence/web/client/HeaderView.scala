package ahlers.presence.web.client

import com.raquo.laminar.api.L._

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object HeaderView {

  def apply(): HtmlElement =
    header(
      SiteMenuView(className := "navbar navbar-expand-lg navbar-dark fixed-top bg-dark"),
      SiteMenuView(className := "navbar navbar-expand-lg navbar-dark bg-dark")
    )

}
