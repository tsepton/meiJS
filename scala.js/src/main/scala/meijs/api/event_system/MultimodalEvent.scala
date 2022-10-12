package meijs.api.event_system

import meijs.factbase.structures.CompletedCommand
import org.scalajs.dom
import org.scalajs.dom.{EventTarget, document}

// @js.native :::: See what it means TODO
class MultimodalEvent(
    typeArg: String = "MultimodalEvent",
    val source: CompletedCommand,
    override val target: EventTarget
) extends dom.Event(typeArg) {
  def emit: Boolean = target.dispatchEvent(this)
}

object MultimodalEvent {
  // TODO : once we have defined a structure for our facts
  def from(fact: CompletedCommand): MultimodalEvent = new MultimodalEvent(
    target = fact.target.getOrElse(document),
    source = fact
  )
}
