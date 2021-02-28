package ahlers.presence.web.client.resume

import ahlers.presence.experiences.{ Experience, ExperienceKey }
import cats.syntax.option._
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._
import laika.api.Transformer
import laika.ast.{ Block, Emphasized, Paragraph, RootElement }
import laika.format.{ AST, Markdown }
import laika.markdown.github.GitHubFlavor
import laika.parse.code.CodeCategory.Markup.Emphasized
import scala.annotation.tailrec

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
              a(
                href(link.url),
                target("_blank"),
                element.content.map(_.toNode))
            case target: InternalTarget =>
              commentNode(s"${target.getClass}")
          }
        case element =>
          commentNode(s"${element.getClass}.")
      }
    }
  }

  def render(
    experienceKey: ExperienceKey,
    experience: Experience,
    $experience: Signal[Experience],
    $focusedExperienceKey: Signal[Option[ExperienceKey]],
    focusedExperienceObserver: Observer[Option[ExperienceKey]]
  ): Div = {
    val headerRender =
      div(
        className("modal-header"),
        h1(
          className("modal-title"),
          child.text <-- $experience.map(_.brief.name.toText)),
        button(
          tpe("button"),
          className("btn-close"),
          onClickClose --> focusedExperienceObserver)
      )

    val bodyRender =
      div(
        className("modal-body"),
        child <--
          $experience
            .map(_.detail.commentary.toText)
            .map(transformer.parser.parse(_).getOrElse(???))
            .map(_.content.toNode)
      )

    val footerRender =
      div(
        className("modal-footer"),
        button(
          tpe("button"),
          className("btn", "btn-secondary"),
          onClickClose --> focusedExperienceObserver,
          "Close"))

    val $isRaised: Signal[Boolean] =
      $focusedExperienceKey.combineWith($experience.map(_.key))
        .mapN(_.contains(_))

    div(
      className("modal", "fade", "d-block"),
      className.toggle("show") <-- $isRaised,
      tabIndex(-1),
      div(
        className("modal-dialog", "modal-dialog-centered", "modal-dialog-scrollable"),
        div(
          className("modal-content"),
          headerRender,
          bodyRender,
          footerRender
        ))
    )
  }

}
