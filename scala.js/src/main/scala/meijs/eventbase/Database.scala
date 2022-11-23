package meijs.eventbase

import meijs.eventbase.structures.Data

import scala.scalajs.js
import scala.util.{Failure, Success, Try}

object Database {

  private var _events: List[Data] = Nil

  def events: List[Data] = _events

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
    * @param i     the index the event should be inserted to
    */
  def insert(event: Data, i: Int): Unit = {
    val (front, back) = Try(_events.splitAt(i)) match {
      case Failure(_)     => (_events, Nil)
      case Success(value) => value
    }
    _events = (front :+ event) ++ back
  }

  /** Alias for append
    */
  def +=(event: Data): Unit = append(event)

  /** Append to the internal events base an element
    *
    * @param event the element to be appended
    */
  def append(event: Data): Unit = _events = _events :+ event

  /** Alias for append
    */
  def ++=(events: List[Data]): Unit = append(events)

  /** Append to the internal factbase a list of elements
    *
    * @param events the list of elements to be appended
    */
  def append(events: List[Data]): Unit = _events = _events :++ events

  def flatMap(f: Data => List[Data]): List[Data] = _events.flatMap(f)

  def map[A](f: Data => A): List[A] = _events.map(f)

  def filter(f: Data => Boolean): List[Data] = _events.filter(f)

  def foreach(f: Data => Boolean): Unit = _events.foreach(f)

  def collect[A](pf: PartialFunction[Data, A]): List[A] =
    _events.collect(pf)

  def contains(data: Data): Boolean = _events.contains(data)

  def sortBy(f: Data => Boolean): List[Data] = _events.sortBy(f)

  def sortWith(f: (Data, Data) => Boolean): List[Data] = _events.sortWith(f)

  def length: Int = _events.length

  /** Should only be used for debug purposes */
  def clear(): Unit = _events = Nil

  override def toString: String = _events.map(data => data.toString).toString

  /** The garbage collector should be the only way to remove data from the internal _events list
    */
  private object GarbageCollector {

    def collect(): Unit = _events = Database._events.filter(_.isValid)

  }
}
