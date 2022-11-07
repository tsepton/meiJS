package meijs.modality.dom

import meijs.eventbase.structures.AtomicEvent
import org.scalajs.{dom => domjs}

final case class DomEvent(name: String, target: domjs.EventTarget)
    extends AtomicEvent

case object DomEvent {
  def fromJSEvent(event: domjs.Event): AtomicEvent = {
    DomEvent(event.`type`, event.target)
  }
}
