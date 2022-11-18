package meijs.api.event_system

import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}
import org.scalajs.dom
import org.scalajs.dom.document

class JavascriptEvent(
    typeArg: String,
    val source: Data
) extends dom.Event(typeArg) {

  def emit: EmittedEvent = {
    document.dispatchEvent(this) // FIXME : what is the target of composite event ?
    EmittedEvent.from(this)
  }
}

object JavascriptEvent {

  def from(source: Data): JavascriptEvent = {
    new JavascriptEvent(
      typeArg = typeArgFrom(source),
      source = source
    )
  }

  def typeArgFrom(source: Data): String = source.event match {
    case event: AtomicEvent => event.name
    case event: CompositeEvent =>
      event.maybeName.getOrElse("Unknown multimodal event")
    case _ => "Unknown multimodal event"
  }
}
