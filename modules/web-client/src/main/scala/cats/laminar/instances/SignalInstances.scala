package cats.laminar.instances

import cats.{ Functor, Invariant, Semigroupal }
import com.raquo.airstream.signal.Signal

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since February 06, 2020
 */
trait SignalInstances {

  implicit object functorSignal extends Functor[Signal] {
    override def map[A, B](fa: Signal[A])(f: A => B) =
      fa.map(f)
  }

  implicit object semigroupalSignal extends Semigroupal[Signal] {
    override def product[A, B](fa: Signal[A], fb: Signal[B]) =
      fa.combineWith(fb)
  }

}
