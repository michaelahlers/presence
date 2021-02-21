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

  //GlobalStyles.addToDocument()
  //GlobalRegistry.addToDocumentOnRegistration()

  //$(".masthead")
  //  .visibility(SemanticUiVisibilitySettings
  //    .once(false)
  //    .onBottomPassed(() =>
  //      $(".fixed.menu")
  //        .transition("fade in"))
  //    .onBottomPassedReverse(() =>
  //      $(".fixed.menu")
  //        .transition("fade out")))
  //
  //$(".ui.sidebar")
  //  .sidebar("attach events", ".toc.item")

  case class Datum(name: String, children: Datum*)

  //val data: Datum =
  //  Datum(
  //    "Root",
  //    Datum("Child 1"),
  //    Datum("Child 2"))

  //val hierarchy: Hierarchy[Datum] =
  //  d3.hierarchy[Datum](data, (_: Datum).children.toJSArray)
  //    .each { node: Hierarchy[Datum] =>
  //      println(s"""name = "%s", depth = %d"""
  //        .format(
  //          node.data.name,
  //          node.depth))
  //    }

  //d3.pack[Datum]()
  //  .size(js.Array(200, 100))
  //  .padding(_ => 2d)
  //  .radius(_ => 10d)
  //  .apply(hierarchy)
  //  .each { node: Hierarchy[Datum] with Packed =>
  //    println(s"""name = "%s", depth = %d, x = %d, y = %d, r = %d"""
  //      .format(
  //        node.data.name,
  //        node.depth,
  //        node.x,
  //        node.y,
  //        node.r))
  //  }

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
    .mapToValue(body(
      className := Seq("d-flex", "flex-column", "h-100"),
      HeaderView(),
      MainView(),
      FooterView()))
    .foreach(render(dom.document.head.parentElement, _))(unsafeWindowOwner)

}
