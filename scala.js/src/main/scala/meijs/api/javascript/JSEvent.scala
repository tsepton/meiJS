package meijs.api.javascript

import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}
import org.scalajs.dom

class JSEvent(typeArg: String, val source: Data) extends dom.Event(typeArg)

object JSEvent {

  def from(source: Data): JSEvent =
    new JSEvent(typeArg = typeArgFrom(source), source = source)

  def typeArgFrom(source: Data): String = source.event match {
    case event: AtomicEvent    => event.name
    case event: CompositeEvent => event.name
    case _                     => "Unknown multimodal event"
  }

}

case class JSEmittedEvent(event: JSEvent) {
  val at: Long = System.currentTimeMillis() / 1000
}

object JSEmittedEvent {
  def from(event: JSEvent): JSEmittedEvent = new JSEmittedEvent(event)
}
