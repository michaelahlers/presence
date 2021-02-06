package cats.laminar.instances

import cats.{ Functor, Semigroupal }
import com.raquo.airstream.eventstream.EventStream

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since February 06, 2021
 */
trait EventStreamInstances {

  implicit object functorEventStream extends Functor[EventStream] {
    override def map[A, B](fa: EventStream[A])(f: A => B) =
      fa.map(f)
  }

  implicit object semigroupalEventStream extends Semigroupal[EventStream] {
    override def product[A, B](fa: EventStream[A], fb: EventStream[B]) =
      fa.combineWith(fb)
  }

}
