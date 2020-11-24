package ahlers.presence.web.client

import com.thoughtworks.binding.Binding
import Binding._
import org.lrng.binding.html
import html.NodeBinding
import org.querki.jquery.$
import org.scalajs.dom.html.{ Input, Table }
import org.scalajs.dom.raw._
import semantic.jquery.SemanticUiVisibilitySettings
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }
import semantic.jquery.syntax._

import scala.concurrent.Future
import scala.scalajs.js

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebClientApplication extends LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logging")

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

  def main(arguments: Array[String]): Unit = {
    logger.info("Hello, World!")

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

  }

  //import org.scalajs.dom.document
  //html.render(document.body, table)

}
