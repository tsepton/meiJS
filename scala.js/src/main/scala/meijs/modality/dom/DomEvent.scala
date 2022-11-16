package meijs.modality.dom

import meijs.eventbase.structures.AtomicEvent
import meijs.modality.{Keyboard, Modality, Mouse}
import org.scalajs.{dom => domjs}

case class DomEvent(name: String, target: domjs.EventTarget, modality: Modality)
    extends AtomicEvent

case object DomEvent {

  def fromMouseEvent(event: domjs.MouseEvent): DomEvent =
    DomEvent(event.`type`, event.target, Mouse)

  def fromKeyboardEvent(event: domjs.KeyboardEvent): DomEvent =
    DomEvent(event.`type`, event.target, Keyboard)

}
