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

  val pageClassName =
    UiState.router.$currentPage
      .map {
        case UiState.Landing => "landing"
        case UiState.Resume => "resume"
        case UiState.Contact => "contact"
      }

  def apply(): HtmlElement =
    main(
      className := "flex-grow-1 mt-5",
      className <-- pageClassName,
      child <-- pageSplitter.$view)

}
