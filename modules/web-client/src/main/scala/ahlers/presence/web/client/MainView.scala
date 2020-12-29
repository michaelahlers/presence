package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import com.raquo.waypoint.SplitRender

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object MainView {

  val pageSplitter =
    SplitRender[UiState, HtmlElement](UiState.router.$currentPage)
      .collectStatic(UiState.Landing)(LandingPageView())
      .collectStatic(UiState.Resume)(ResumePage())
      .collectStatic(UiState.Contact)(ContactPage())

  def apply(): HtmlElement =
    main(
      className := "flex-grow-1",
      child <-- pageSplitter.$view)

}
