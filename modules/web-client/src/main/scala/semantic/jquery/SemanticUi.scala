package semantic.jquery

import org.querki.jquery.JQuery

import scala.scalajs.js

/**
 * @since November 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
@js.native
sealed trait SemanticUi extends js.Any { self:JQuery =>

  def dropdown(xs: js.Any*): JQuery with SemanticUi = js.native

  def sidebar(xs: js.Any*): JQuery with SemanticUi = js.native

  def search(xs: js.Any*): JQuery with SemanticUi = js.native

  def transition(xs: js.Any*): JQuery with SemanticUi = js.native

  def visibility(settings: SemanticUiVisibilitySettings): JQuery with SemanticUi = js.native

}
