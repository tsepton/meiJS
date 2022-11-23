package meijs.api.javascript

import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data, Event}
import org.scalajs.dom

import scala.concurrent.duration.FiniteDuration
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichIterableOnce

class JSEvent(typeArg: String, data: Data) extends dom.Event(typeArg) {

  val source: Event = data.event

  val validationDelay: FiniteDuration = data.validationDelay

  val emissionTime: Long = data.emissionTime

  def isValid: Boolean = validUntil > (System.currentTimeMillis / 1000)

  def validUntil: Long = emissionTime + validationDelay.toSeconds

  def occurrences: js.Array[AtomicEvent] =
    data.occurrences.toJSArray

}

object JSEvent {

  def from(source: Data): JSEvent =
    new JSEvent(typeArg = typeArgFrom(source), source)

  def typeArgFrom(source: Data): String = source.event match {
    case event: AtomicEvent    => event.name
    case event: CompositeEvent => event.name
    case _                     => "Unknown multimodal event"
  }

}
