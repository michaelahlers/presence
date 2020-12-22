package ahlers.presence.web.client

import ahlers.presence.web.client.CssSettings._
import scalacss.internal.mutable.StyleSheet.Inline
import semantic.SemanticStyles

/**
 * @since December 22, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object GlobalStyles extends Inline {
  import dsl._

  val semantic = new SemanticStyles()

  val common = mixin(
    backgroundColor.green)

  val outer = style(
    common)
}
