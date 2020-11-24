package semantic.jquery

import org.querki.jquery.JQuery

/**
 * @since November 23, 2020
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 */
object syntax {

  implicit def implySemanticMethods(jQuery: JQuery): JQuery with SemanticMethods =
    jQuery.asInstanceOf[JQuery with SemanticMethods]

}
