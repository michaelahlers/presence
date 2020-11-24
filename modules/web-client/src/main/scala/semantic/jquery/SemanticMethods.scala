package semantic.jquery

import org.querki.jquery.JQuery

import scala.scalajs.js

/**
 * @since November 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
@js.native
sealed trait SemanticMethods extends js.Any {

  def dropdown(params: js.Any*): JQuery with SemanticMethods = js.native

  def sidebar(params: js.Any*): JQuery with SemanticMethods = js.native

  def search(params: js.Any*): JQuery with SemanticMethods = js.native

  def transition(params: js.Any*): JQuery with SemanticMethods = js.native

  def visibility(settings: SemanticVisibilitySettings): JQuery with SemanticMethods = js.native

}
