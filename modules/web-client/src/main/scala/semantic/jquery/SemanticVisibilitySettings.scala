package semantic.jquery

import org.querki.jsext.{ noOpts, JSOptionBuilder, OptMap }

import scala.scalajs.js

/**
 * @since November 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
trait SemanticVisibilitySettings extends js.Object {}
object SemanticVisibilitySettings extends SemanticVisibilitySettingsBuilder(noOpts)
class SemanticVisibilitySettingsBuilder(val dict: OptMap) extends JSOptionBuilder[SemanticVisibilitySettings, SemanticVisibilitySettingsBuilder](new SemanticVisibilitySettingsBuilder(_)) {

  def once(v: Boolean) = jsOpt("once", v)
  def onBottomPassed(f: js.Function0[js.Any]) = jsOpt("onBottomPassed", f)
  def onBottomPassedReverse(f: js.Function0[js.Any]) = jsOpt("onBottomPassedReverse", f)

}
