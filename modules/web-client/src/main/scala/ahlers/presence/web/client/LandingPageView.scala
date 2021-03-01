package ahlers.presence.web.client

import com.raquo.laminar.api.L._

import java.time.{ LocalDate, Period, ZoneOffset }

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object LandingPageView {

  val experiencePeriod: EventStream[Period] =
    EventStream
      .periodic(1000)
      .mapTo(LocalDate
        .of(2000, 5, 1)
        .until(LocalDate.now(ZoneOffset.UTC)))

  def apply(): HtmlElement =
    article(
      className("container-fluid", "mt-5", "pt-3"),
      div(
        className("row", "d-flex", "justify-content-center", "mt-3"),
        div(
          className("col-xl-5", "col-lg-6", "col-md-7", "col-sm-8", "col-11"),
          LogoView())
      ),
      div(
        className("row", "mt-3"),
        div(
          className("col-12"),
          h1(className("display-6", "text-center", "text-muted", "text-lowercase", "fw-lighter"), "Michael Ahlers Consulting"))
      ),
      div(
        className("row", "d-flex", "justify-content-center", "mt-3 mb-3"),
        div(
          className("col-xl-7", "col-lg-8", "col-md-9", "col-sm-10", "col-12"),
          p(
            className("lead"),
            "Michael Ahlers is a software engineer—with over ",
            child.text <-- experiencePeriod.map(_.getYears()),
            " years of professional experience—who views the practice through an engineering lens, applying tenacious attention to detail. When not working, Michael enjoys competitive bike racing and recreational flying as a certified sport pilot."
          )
        )
      )
    )

}
