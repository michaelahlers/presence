package ahlers.presence.web.client

import ahlers.presence.web.client.Router.Action.{ Follow, Replace }
import ahlers.presence.web.client.Router.{ Action, Rule }
import cats.Invariant
import com.thoughtworks.binding.Binding.{ SingleMountPoint, Var }
import org.scalajs.dom.raw.Event
import org.scalajs.dom.window
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }
import trail.Route

import scala.reflect.ClassTag

/**
 * @since November 25, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
class Router[A](rules: Seq[Rule[A]] = Seq.empty, action: Var[Action[A]]) extends SingleMountPoint[Action[A]](action) with LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logs")

  /** Called when [[action]] is changed. */
  override protected def set(action: Action[A]) =
    action match {

      case Follow(state) =>
        val url = uri(state)
        logger.info(s"Router set state: follow $url to $state.")
        window.history.pushState(
          null,
          null,
          url)

      case Replace(state) =>
        val url = uri(state)
        logger.info(s"Router set state: replace $url to $state.")
        window.history.replaceState(
          null,
          null,
          url)

    }

  private def updateState() =
    rules
      .flatMap(_.decode(window.location.href)) match {
      case Seq() => ???
      case Seq((_, state)) => Replace(state)
      case _ => ???
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

  def uri(state: A): String =
    rules
      .flatMap(_.encode(state)) match {
      case Seq() => ???
      case Seq(uri) => uri
      case _ => ???
    }

  def follow(state: A): Unit =
    action.value =
      Follow(uri(state), state)

}

object Router {

  implicit object invariantRoute extends Invariant[Route] {

    override def imap[A, B](fa: Route[A])(f: A => B)(g: B => A): Route[B] = ???

  }

  trait Rule[A] {
    def encode(x: A): Option[String]
    def decode(x: String): Option[A]
  }

  case class RuleRoute[A, B](route: Route[B], f: B => A, g: A => B)(implicit A: ClassTag[A]) extends Rule[A] {

    override def encode(x: A) =
      x match {
        case A(x) => Some(route.url(g(x)))
        case _ => None
      }

    override def decode(x: String) =
      route
        .parseArgs(x)
        .map(f(_))

  }

  sealed trait Action[A] {
    def state: A
  }

  object Action {
    case class Replace[A](state: A) extends Action[A]
    case class Follow[A](state: A) extends Action[A]
  }

}
