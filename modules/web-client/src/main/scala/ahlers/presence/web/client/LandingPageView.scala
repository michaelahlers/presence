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
          className := "col-4",
          img(src := Asset.versioned("ahlers/presence/web/client/Ahlers Consulting (dark).svg").url)),
        div(
          className := "col-4 text-nowrap",
          h1(className := "title", "Michael Ahlers"),
          //h2(className := "text-lowercase text-muted", "Software Engineer"),
          ContactInformationView()
        )
      ),
      div(
        className := "row justify-content-center mt-5 mb-3",
        div(
          className := "col-7",
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
