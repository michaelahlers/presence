package ahlers.presence.web.client

import ahlers.presence.web.client.Router.Action.Replace
import ahlers.presence.web.client.Router.RuleRoute
import com.thoughtworks.binding.Binding
import com.thoughtworks.binding.Binding._
import org.lrng.binding.html
import org.lrng.binding.html.NodeBinding
import org.scalajs.dom.html.Input
import org.scalajs.dom.raw._
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

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
    //logger.info("Hello, World!")

    //logger.info(s"${window.location.pathname}")
    //logger.info(s"${window.history}")

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

//sealed trait Action
//case class Follow(state: String) extends Action
//case class Direct(state: String) extends Action
//
//class Router(val state: Var[Action]) extends SingleMountPoint[Action](state) {
//  import trail._
//
//  val HomeRoute: Route[Unit] = Root / "home"
//  val WorkRoute: Route[Unit] = Root / "work"
//  val CompanyRoute: Route[Unit] = Root / "company"
//  val CareersRoute: Route[Unit] = Root / "careers"
//
//  def uri(page: String): String =
//    page match {
//      case "home" => HomeRoute.url(())
//      case "work" => WorkRoute.url(())
//      case "company" => CompanyRoute.url(())
//      case "careers" => CareersRoute.url(())
//    }
//
//  /** Called when [[state]] is changed. */
//  override protected def set(action: Action) = {
//    logger.info(s"Router set state: $action.")
//    action match {
//
//      case Follow(page) =>
//        window.history.pushState(
//          null,
//          null,
//          uri(page))
//
//      case Direct(page) =>
//        window.history.replaceState(
//          null,
//          null,
//          uri(page))
//
//    }
//
//  }
//
//  def updateState() = {
//    import trail._
//    val HomeRoute: Route[Unit] = Root / "home"
//    val WorkRoute: Route[Unit] = Root / "work"
//    val CompanyRoute: Route[Unit] = Root / "company"
//    val CareersRoute: Route[Unit] = Root / "careers"
//
//    window.location.href match {
//      case route @ HomeRoute(_) =>
//        logger.info(s"Router update state: home, ${window.location.href}.")
//        state.value = Direct("home")
//      case route @ WorkRoute(_) =>
//        logger.info(s"Router update state: work, ${window.location.href}.")
//        state.value = Direct("work")
//      case route @ CompanyRoute(_) =>
//        logger.info(s"Router update state: company, ${window.location.href}.")
//        state.value = Direct("company")
//      case route @ CareersRoute(_) =>
//        logger.info(s"Router update state: careers, ${window.location.href}.")
//        state.value = Direct("careers")
//    }
//  }
//
//  val listener = { _: Event =>
//    updateState()
//  }
//
//  override protected def mount() = {
//    updateState()
//    super.mount()
//    window.addEventListener("popstate", listener)
//  }
//
//  override protected def unmount() = {
//    window.removeEventListener("popstate", listener)
//    super.unmount()
//  }
//}
//
//object router extends Router(Var(Direct("???")))
//router.watch()

//$("a").on(
//  "click",
//  { event: Event =>
//    if (event.target == event.currentTarget) event.preventDefault()
//    event.stopPropagation()
//    event.target match {
//      case anchor: HTMLAnchorElement =>
//        logger.info(s"anchor.href: ${anchor.href}, anchor.pathname: ${anchor.pathname}")
//        window.history.pushState(null, null, anchor.href)
//        route.updateState()
//    }
//
//  }
//)

    import trail._

    object router
      extends Router[String](
        Seq(
          RuleRoute[String, Unit](Root / "home", _ => "home", _ => ()),
          RuleRoute[String, Unit](Root / "work", _ => "work", _ => ()),
          RuleRoute[String, Unit](Root / "company", _ => "company", _ => ()),
          RuleRoute[String, Unit](Root / "careers", _ => "careers", _ => ())
        ),
        Var(Replace("home")))

    @html def menu = {
      def onClick(state: String) = { event: Event =>
        if (event.target == event.currentTarget) event.preventDefault()
        event.stopPropagation()
        event.target match {
          case anchor: HTMLAnchorElement =>
            logger.info(s"Clicked anchor: ${anchor.href}, ${anchor.pathname}")
            router.follow(state)
        }
      }

      <div class="ui inverted vertical masthead center aligned segment">
        <div class="ui container">
          <div class="ui large secondary inverted pointing menu">
            <a class="toc item">
              <i class="sidebar icon"></i>
            </a>
            <a class="active item" onclick={onClick("home")} href={router.reverse("home")}>Home</a>
            <a class="item" onclick={onClick("work")} href={router.reverse("work")}>Work</a>
            <a class="item" onclick={onClick("company")} href={router.reverse("company")}>Company</a>
            <a class="item" onclick={onClick("careers")} href={router.reverse("careers")}>Careers</a>
            <div class="right item">
              <a class="ui inverted button">Log in</a>
              <a class="ui inverted button">Sign Up</a>
            </div>
          </div>
        </div>
      </div>
    }

    import org.scalajs.dom.document
    html.render(document.body, menu)

  }

}
