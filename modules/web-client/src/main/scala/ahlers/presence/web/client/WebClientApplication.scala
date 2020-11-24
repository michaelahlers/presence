package ahlers.presence.web.client

import com.thoughtworks.binding.Binding
import Binding._
import org.lrng.binding.html
import html.NodeBinding
import org.querki.jquery.$
import org.scalajs.dom.html.{ Input, Table }
import org.scalajs.dom.raw._
import org.scalajs.dom.window
import semantic.jquery.SemanticUiVisibilitySettings
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }
import semantic.jquery.syntax._

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Object.{ getOwnPropertyNames, isExtensible }

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebClientApplication extends LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")

  case class Contact(name: Var[String], email: Var[String])

  val contacts = Vars.empty[Contact]

  @html val inputName: NodeBinding[Input] = <input type="text" />

  @html val inputEmail: NodeBinding[Input] = <input type="text" />

  @html
  val table =
    <div>

    {inputName.bind}
    {inputEmail.bind}

      <button
      onclick={
      event: Event =>
        val name: Var[String] = Var("Michael Ahlers")
        val email: Var[String] = Var("michael@ahlers.consulting")

        name.value = inputName.value.value
        email.value = inputEmail.value.value

        val contact = Contact(name, email)
        logger.info(s"""Added contact ${contact.name.value}.""")
        contacts.value += contact
    }>
        Add a contact
      </button>
    </div>

    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>E-mail</th>
        </tr>
      </thead>
      <tbody>
        {
      for (contact <- contacts)
        yield <tr>
            <td>
              {contact.name.bind}
            </td>
            <td>
              {contact.email.bind}
            </td>
          </tr>
    }
      </tbody>
    </table>

  //sealed trait Page
  //case object Home extends Page
  //case object Work extends Page
  //case object Company extends Page
  //case object Careers extends Page

  def main(arguments: Array[String]): Unit = {
    logger.info("Hello, World!")

    logger.info(s"${window.location.pathname}")
    logger.info(s"${window.history}")

    $(".masthead")
      .visibility(SemanticUiVisibilitySettings
        .once(false)
        .onBottomPassed(() =>
          $(".fixed.menu")
            .transition("fade in"))
        .onBottomPassedReverse(() =>
          $(".fixed.menu")
            .transition("fade out")))

    $(".ui.sidebar")
      .sidebar("attach events", ".toc.item")

    class Router(state: Var[String]) extends SingleMountPoint[String](state) {
      import trail._

      val HomeRoute: Route[Unit] = Root / "home"
      val WorkRoute: Route[Unit] = Root / "work"
      val CompanyRoute: Route[Unit] = Root / "company"
      val CareersRoute: Route[Unit] = Root / "careers"

      override protected def set(value: String) =
        window.history.replaceState(
          null,
          null,
          value match {
            case "home" => HomeRoute.url(())
            case "work" => WorkRoute.url(())
            case "company" => CompanyRoute.url(())
            case "careers" => CareersRoute.url(())
          }
        )

      def updateState() = {
        import trail._
        val HomeRoute: Route[Unit] = Root / "home"
        val WorkRoute: Route[Unit] = Root / "work"
        val CompanyRoute: Route[Unit] = Root / "company"
        val CareersRoute: Route[Unit] = Root / "careers"

        window.location.href match {
          case route @ HomeRoute(_) =>
            logger.info(s"Home: $route")
            state.value = "home"
          case route @ WorkRoute(_) =>
            logger.info(s"Work: $route")
            state.value = "work"
          case route @ CompanyRoute(_) =>
            logger.info(s"Company: $route")
            state.value = "company"
          case route @ CareersRoute(_) =>
            logger.info(s"Careers: $route")
            state.value = "careers"
        }
      }

      val listener = { _: Event =>
        updateState()
      }

      override protected def mount() = {
        updateState()
        super.mount()
        window.addEventListener("popstate", listener)
      }

      override protected def unmount() = {
        window.removeEventListener("popstate", listener)
        super.unmount()
      }
    }

    object route extends Router(Var("home"))
    route.watch()

    $("a").on(
      "click",
      { event: Event =>
        if (event.target == event.currentTarget) event.preventDefault()
        event.stopPropagation()
        event.target match {
          case anchor: HTMLAnchorElement =>
            logger.info(s"anchor.href: ${anchor.href}, anchor.pathname: ${anchor.pathname}")
            window.history.pushState(null, null, anchor.href)
            route.updateState()
        }

      }
    )

  }

  //import org.scalajs.dom.document
  //html.render(document.body, table)

}
