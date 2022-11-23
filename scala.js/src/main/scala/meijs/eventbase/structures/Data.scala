package meijs.eventbase.structures

import scala.concurrent.duration._
import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

/** Represents an event occurrence.
  * Therefore, it is used inside the Database as the representation of an event that happened.
  */
trait Data {

  import scala.scalajs.js.JSConverters._

  @JSExport
  val validationDelay: FiniteDuration = 1.seconds // TODO

  @JSExport
  val emissionTime: Long = System.currentTimeMillis / 1000

  @JSExport
  val event: Event // Event description

  val occurrence: List[AtomicEvent]

  @JSExport("occurrence")
  def jsOccurrence: js.Array[AtomicEvent] = occurrence.toJSArray

  @JSExport
  def isValid: Boolean = validUntil > (System.currentTimeMillis / 1000)

  @JSExport
  def validUntil: Long = emissionTime + validationDelay.toSeconds

  override def toString: String =
    f"""Data with event: $event
       |   emissionTime: $emissionTime
       |   validUntil: $validUntil""".stripMargin
}

case object Data {

  def from(e: AtomicEvent): Data = new Data {
    val event: Event                  = e
    val occurrence: List[AtomicEvent] = List(e)
  }

  def from(events: List[AtomicEvent]): List[Data] =
    events.map(from)

  def from(
      event: CompositeEvent,
      occurrence: List[AtomicEvent]
  ): Data = {
    val e = event
    val o = occurrence
    new Data {
      val event: Event                  = e
      val occurrence: List[AtomicEvent] = o
    }
  }

}
