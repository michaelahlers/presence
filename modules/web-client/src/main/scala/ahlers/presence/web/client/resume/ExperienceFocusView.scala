package ahlers.presence.web.client.resume

import ahlers.presence.experiences.{ Experience, ExperienceKey }
import ahlers.presence.web.client.UiState
import ahlers.presence.web.client.UiState.FocusedResumePage
import cats.syntax.option._
import com.raquo.laminar.api.L._
import io.lemonlabs.uri.Url
import laika.api.Transformer
import laika.format.{ AST, Markdown }
import laika.markdown.github.GitHubFlavor
import org.scalajs.dom

import scala.annotation.tailrec
import scala.util.{ Failure, Success, Try }

/**
 * @since February 20, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceFocusView {

  val onClickClose =
    onClick
      .stopPropagation
      .mapToStrict(none)

  val transformer =
    Transformer
      .from(Markdown)
      .to(AST)
      .using(GitHubFlavor)
      .build

  implicit class LaikaElementSyntax(private val element: laika.ast.Element) {

    /**
     * Converts a [[laika Laika]] [[laika.ast.Element]] to a reactive [[Node]].
     * @todo Formalize and test—this is, oh, so much a work-in-progress proof-of-concept.
     * @todo Make [[tailrec]]. 😬
     */
    def toNode: Node = {
      import laika.ast._
      element match {

        case element: RootElement =>
          div(element.content.map(_.toNode))

        case element: Paragraph =>
          p(element.content.map(_.toNode))

        case element: Text =>
          textToNode(element.content)

        case element: Emphasized =>
          i(element.content.map(_.toNode))

        case element: SpanLink =>
          element.target match {

            case link: ExternalTarget =>
              Try(UiState.router.pageForRelativeUrl(link.url)) match {
                case Failure(_) | Success(None) =>
                  a(
                    href(link.url),
                    target("_blank"),
                    rel("noopener", "noreferrer"),
                    span(element.content.map(_.toNode)),
                    sup(i(className("fas", "fa-external-link-alt")))
                  )

                case Success(Some(uiState)) =>
                  a(
                    href(link.url),
                    onClick.preventDefault.mapToStrict(uiState) --> (UiState.router.pushState(_)),
                    element.content.map(_.toNode))

              }

            case link =>
              commentNode(s"${link.getClass}")

          }

        case element: BulletList =>
          ul(element.content.map(_.toNode))

        /** [[BulletListItem.content]] must have type [[Block]] (for nesting), but when its content is only a single [[Text]] do not emit a [[p]]. */
        case BulletListItem(Seq(Paragraph(content @ Seq(_: Text), _)), _, _) =>
          li(content.map(_.toNode))

        case element: BulletListItem =>
          li(element.content.map(_.toNode))

        /** @todo Determine if this is correct. */
        case element: SpanSequence =>
          span(element.content.map(_.toNode))

        case element: Literal =>
          code(element.content)

        case element: QuotedBlock =>
          blockQuote(element.content.map(_.toNode))

        case element =>
          commentNode(s"${element.getClass}.")
      }
    }
  }

  def render(
    key: ExperienceKey,
    experience: Experience,
    $experience: Signal[Experience],
    $focusedExperience: Signal[Option[Experience]],
    focusedKeyObserver: Observer[Option[ExperienceKey]],
    $experiences: Signal[Seq[Experience]]
  ): Div = {
    val headerRender =
      div(
        className("modal-header"),
        h3(
          className("modal-title"),
          child.text <-- $experience.map(_.brief.name.toText)),
        button(
          tpe("button"),
          className("btn-close"),
          onClickClose --> focusedKeyObserver)
      )

    val bodyRender =
      div(
        className("modal-body"),
        h4("Summary"),
        div(
          child <--
            $experience
              .map(_.detail.summary.toText)
              .map(transformer.parser
                .parse(_)
                .fold(error => p(s"""Couldn't render summary. ${error.message}"""), _.content.toNode))),
        h4("Commentary"),
        div(
          child <--
            $experience
              .map(_.detail.commentary.toText)
              .map(transformer.parser
                .parse(_)
                .fold(error => p(s"""Couldn't render commentary. ${error.message}"""), _.content.toNode))),
        h4("See also"),
        p("Related experiences."),
        div(
          className("container-fluid"),
          div(
            className("row", "g-3"),
            children <--
              $experiences.combineWith($experience.map(_.adjacents.map(_.key)))
                .mapN((experiences, keys) => experiences.filter(e => keys.contains(e.key)))
                .map(_.map { adjacent =>
                  val uiState = FocusedResumePage(adjacent.key)
                  div(
                    className( /*"col-xl-1",*/ /*"col-lg-",*/ "col-lg-1", /*"col-sm-",*/ "col-2"),
                    a(
                      href(UiState.router.relativeUrlForPage(uiState)),
                      onClick.preventDefault.mapToStrict(uiState) --> (UiState.router.pushState(_)),
                      img(src(adjacent.brief.logo.toText))
                    )
                  )
                })
          )
        )
      )

    val footerRender =
      div(
        className("modal-footer"),
        button(
          tpe("button"),
          className("btn", "btn-secondary"),
          onClickClose --> focusedKeyObserver,
          "Close"))

    val $isRaised: Signal[Boolean] =
      $focusedExperience.combineWith($experience.map(_.key))
        .mapN(_.map(_.key).contains(_))

    div(
      className("modal", "fade", "d-block"),
      className.toggle("show") <-- $isRaised,
      tabIndex(-1),
      div(
        className("modal-dialog", "modal-lg", "modal-dialog-centered", "modal-dialog-scrollable"),
        div(
          className("modal-content"),
          headerRender,
          bodyRender,
          footerRender
        )
      )
    )
  }

}
