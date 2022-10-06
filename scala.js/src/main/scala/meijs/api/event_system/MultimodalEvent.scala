package meijs.api.event_system

import meijs.structures.CompletedCommand
import org.scalajs.dom
import org.scalajs.dom.{EventTarget, document}

import scala.scalajs.js

// @js.native :::: See what it means TODO
class MultimodalEvent(
    typeArg: String = "MultimodalEvent",
    override val target: EventTarget
) extends dom.Event(typeArg) {}

object MultimodalEvent {
  // TODO : once we have defined a structure for our facts
  def from(fact: CompletedCommand): MultimodalEvent = new MultimodalEvent(target = fact.target.getOrElse(document))
}
