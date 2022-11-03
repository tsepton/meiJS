package meijs.eventbase

import meijs.eventbase.structures.Event

import scala.scalajs.js
import scala.util.{Failure, Success, Try}

// @ScalaJSDefined
object EventBase {

  private var _events: List[Event] = Nil

  /** Set an interval for the garbage collector object to run and remove all the invalid data inside the event base
    *
    * @param ms the time in milliseconds for an interval to run the garbage collector task
    */
  def init(ms: Int): Unit = js.timers.setInterval(ms) {
    GarbageCollector.collect()
  }

  /** Insert a *event* at the given *index*
    *
    * Note: if (i > _events.length), then the event is appended
    *
    * @param event a new element to insert at position i
    * @param i    the index the event should be inserted to
    */
  def insert(event: Event, i: Int): Unit = {
    val (front, back) = Try(_events.splitAt(i)) match {
      case Failure(_)     => (_events, Nil)
      case Success(value) => value
    }
    _events = (front :+ event) ++ back
  }

  /** Alias for append
    */
  def +=(event: Event): Unit = append(event)

  /** Append to the internal events base an element
    *
    * @param event the element to be appended
    */
  def append(event: Event): Unit = _events = _events :+ event

  /** Alias for append
    */
  def ++=(events: List[Event]): Unit = append(events)

  /** Append to the internal factbase a list of elements
    *
    * @param events the list of elements to be appended
    */
  def append(events: List[Event]): Unit = _events = _events :++ events

  def flatMap(f: Event => List[Event]): List[Event] = _events.flatMap(f)

  def map[A](f: Event => A): List[A] = _events.map(f)

  def filter(f: Event => Boolean): List[Event] = _events.filter(f)

  def collect[A <: Event](pf: PartialFunction[Event, A]): List[A] =
    _events.collect(pf)

  override def toString: String = _events.map(event => "test").toString

  /** The garbage collector should be the only way to remove data from the internal _events list
    */
  private object GarbageCollector {

    def collect(): Unit = _events = EventBase._events.filter(_.isValid)

  }
}
