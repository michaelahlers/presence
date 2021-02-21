//package ahlers.presence.web.client.resume
//
//import com.raquo.domtypes.generic.Modifier
//import com.raquo.laminar.api.L._
//import com.raquo.laminar.nodes._
//import org.scalajs.dom.svg._
//
///**
// * @since January 31, 2021
// * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
// */
//object ExperienceBriefFocusView {
//
//  def render(
//    briefState: ExperienceBriefState.Brief,
//    modifiers: Modifier[ReactiveSvgElement[G]]*
//  ): ReactiveSvgElement[G] = {
//    import svg._
//
//    g(
//      className := Seq("experience-focus-view"),
//      image(
//        xlinkHref := briefState.logo,
//        x := briefState.x.toString,
//        y := briefState.y.toString,
//        width := briefState.width.toString,
//        height := briefState.height.toString
//      ),
//      modifiers
//    )
//  }
//
//}
