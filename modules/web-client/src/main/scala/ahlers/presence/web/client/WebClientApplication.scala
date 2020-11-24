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
import scala.scalajs.js.Object.getOwnPropertyNames

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

    def checkState() = {
      import trail._
      val HomeRoute: Route[Unit] = Root / "home"
      val WorkRoute: Route[Unit] = Root / "work"
      val CompanyRoute: Route[Unit] = Root / "company"
      val CareersRoute: Route[Unit] = Root / "careers"

      window.location.href match {
        case route @ HomeRoute(_) => logger.info(s"Home: $route")
        case route @ WorkRoute(_) => logger.info(s"Work: $route")
        case route @ CompanyRoute(_) => logger.info(s"Company: $route")
        case route @ CareersRoute(_) => logger.info(s"Careers: $route")
      }
    }

    checkState()
    window.onpopstate = { _ =>
      checkState()
    }

    $("a").on(
      "click",
      { event: Event =>
        if (event.target == event.currentTarget) event.preventDefault()
        event.stopPropagation()
        event.target match {
          case anchor: HTMLAnchorElement =>
            logger.info(s"anchor.href: ${anchor.href}, anchor.pathname: ${anchor.pathname}")
            window.history.pushState(null, null, anchor.href)
            checkState()
        }

      }
    )

  }

  //import org.scalajs.dom.document
  //html.render(document.body, table)

}
