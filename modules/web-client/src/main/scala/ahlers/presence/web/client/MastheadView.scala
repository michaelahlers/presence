package ahlers.presence.web.client

import com.raquo.laminar.api.L._

import java.time.{ LocalDate, Period, ZoneId }

/**
 * @since December 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object MastheadView {

  val $tick = EventStream.periodic(1000)

  val startDate =
    LocalDate.of(2000, 7, 1)

  val experiencePeriod: EventStream[Period] =
    $tick.map { _ =>
      Period.between(
        startDate,
        LocalDate.now(ZoneId.of("America/New_York")))
    }

  def apply(): Div =
    div(
      h1(
        className := "logo",
        img(
          width := "300px",
          src := Asset.versioned("ahlers/presence/web/client/Ahlers Consulting (dark).svg").url)),
      p(
        "Michael Ahlers is a software developer—with over ",
        child.text <-- experiencePeriod.map(_.getYears()).map("%d years".format(_)),
        " of professional experience—who views the practice through an engineering lens, applying tenacious attention to detail. When not working, Michael enjoys competitive bike racing and recreational flying as a certified sport pilot."
      )
    )

}
