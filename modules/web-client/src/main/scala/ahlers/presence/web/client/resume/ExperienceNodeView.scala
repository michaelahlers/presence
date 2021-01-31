package ahlers.presence.web.client.resume

import com.raquo.laminar.api.L.Signal
import com.raquo.laminar.api.L.Observer
import com.raquo.laminar.api.L.svg._
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.svg.{ G, Image }

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
case class ExperienceNodeView(
  brief: ExperienceBrief,
  element: ReactiveSvgElement[G])

object ExperienceNodeView {

  import ExperienceBrief.Blank
  import ExperienceBrief.Employment
  import ExperienceBrief.Skill

  def apply(
    brief: ExperienceBrief,
    $state: Signal[ExperienceNodeState],
    focusObserver: Observer[ExperienceNodeState]
  ): ExperienceNodeView = {
    val radius: Double = 20d

    val icon: ReactiveSvgElement[Image] =
      brief match {

        case Blank => ???

        case employment: Employment =>
          image(
            xlinkHref := employment.logo,
            x <-- $state.map(_.x - radius).map(_.toString),
            y <-- $state.map(_.y - radius).map(_.toString),
            width := (radius * 2d).toString,
            height := (radius * 2d).toString
          )

        case skill: Skill =>
          image(
            xlinkHref := skill.logo,
            x <-- $state.map(_.x - radius).map(_.toString),
            y <-- $state.map(_.y - radius).map(_.toString),
            width := (radius * 2d).toString,
            height := (radius * 2d).toString
          )

      }

    val node: ReactiveSvgElement[G] =
      g(
        className := "experience-node-view",
        icon)

    ExperienceNodeView(
      brief,
      node)
  }

}
