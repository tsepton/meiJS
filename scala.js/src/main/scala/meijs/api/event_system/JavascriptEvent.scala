package meijs.api.event_system

import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}
import org.scalajs.dom
import org.scalajs.dom.{EventTarget, document}

class JavascriptEvent(
    typeArg: String,
    val source: Data,
    override val target: EventTarget
) extends dom.Event(typeArg) {

  def emit: EmittedEvent = {
    target.dispatchEvent(this)
    EmittedEvent.from(this)
  }

}

object JavascriptEvent {
  def from(source: Data): JavascriptEvent = new JavascriptEvent(
    typeArg = source.event match {
      case event: AtomicEvent => event.name
      case event: CompositeEvent =>
        event.maybeName.getOrElse("Unknown multimodal event")
      case _ => "Unknown multimodal event"
    },
    target = document, //source.target.getOrElse(document), TODO
    source = source
  )
}
