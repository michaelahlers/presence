package ahlers.presence.web.client

import ahlers.presence.web.client.UiState.ResumePage
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
      .collectSignal[ResumePage](ResumeView.render(_))
      .collectStatic(UiState.Contact)(ContactPage())

  def apply(): HtmlElement =
    main(
      child <-- pageSplitter.$view)

}
