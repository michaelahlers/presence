package ahlers.presence.web.client

import com.raquo.laminar.api.L._
import org.scalajs.dom
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebClientApplication extends App with LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")

  /** Bootstrap utility classes can't be assigned to parents using modifiers. */
  Seq("h-100")
    .foreach(dom
      .document
      .head
      .parentElement
      .classList
      .add(_))

  documentEvents
    .onDomContentLoaded
    .mapToStrict(body(
      className("d-flex", "flex-column", "h-100"),
      className.toggle("modal-open") <-- UiState.$modals.map(_.nonEmpty),
      HeaderView(),
      MainView(),
      FooterView()))
    .foreach(render(dom.document.head.parentElement, _))(unsafeWindowOwner)

}
