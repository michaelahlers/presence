package ahlers.presence.web.client.resume

import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.api.L._

/**
 * @since January 31, 2021
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object ExperienceLabelView {

  def render(text: String, modifiers: Modifier[Element]*) =
    label(
      xmlns := "http://www.w3.org/1999/xhtml",
      className := "experience-label-view",
      className := "rounded-pill",
      text,
      modifiers)

}
