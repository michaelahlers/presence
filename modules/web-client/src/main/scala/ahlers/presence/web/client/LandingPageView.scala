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
      className := "container",
      div(
        className := "row justify-content-center mt-5",
        div(
          className := "col-xl-3 col-lg-4 col-md-5 col-9",
          img(src := Asset.versioned("ahlers/presence/web/client/Ahlers Consulting (dark).svg").url)),
        div(
          className := "col-xl-3 col-lg-4 col-md-5 col-6 mt-md-auto mt-4",
          h1(className := "title text-nowrap", "Michael Ahlers"),
          //h2(className := "text-lowercase text-muted", "Software Engineer"),
          ContactInformationView()
        )
      ),
      div(
        className := "row justify-content-center mt-md-5 mt-4 mb-3",
        div(
          className := "col-xl-7 col-lg-8 col-md-9 col-12",
          p(
            className := "lead",
            child.text <--
              experiencePeriod
                .map(_.getYears())
                .map("Michael Ahlers is a software developer—with over %d years of professional experience—who views the practice through an engineering lens, applying tenacious attention to detail. When not working, Michael enjoys competitive bike racing and recreational flying as a certified sport pilot."
                  .format(_))
          )
        )
      )
    )

}
