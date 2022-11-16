package meijs.api.event_system

import meijs.eventbase.structures.Data
import org.scalajs.dom
import org.scalajs.dom.{EventTarget, document}

class JavascriptEvent(
    typeArg: String = "MultimodalEvent",
    val source: Data,
    override val target: EventTarget
) extends dom.Event(typeArg) {

  def emit: EmittedEvent = {
    target.dispatchEvent(this)
    EmittedEvent.from(this)
  }

}

object JavascriptEvent {
  // TODO : once we have defined a structure for our facts
  def from(source: Data): JavascriptEvent = new JavascriptEvent(
    target = document, //source.target.getOrElse(document), TODO
    source = source
  )
}
