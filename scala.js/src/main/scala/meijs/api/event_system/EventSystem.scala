package meijs.api.event_system

import meijs.eventbase.structures.Data
import meijs.eventbase.{Database, Registry}

import scala.scalajs.js

object EventSystem {

  private var emitted: List[EmittedEvent] = Nil

  /** Set an interval which will collect and emit all completed MultimodalEvent in the fact base
    *
    * @param ms : the time interval in milliseconds between each collect of events
    */
  def init(ms: Int): Unit = {
    js.timers.setInterval(ms)(handleEmission())
    js.timers.setInterval(ms * 10)(GarbageCollector.collect())
  }

  private def handleEmission(): Unit = {
    val commands = Database
      .filter(data => Registry.list contains data.event)
      .filterNot(data => emitted.map(_.event.source).contains(data))
      .collect { case data: Data => JavascriptEvent from data }
    emitted ++= commands.map(_.emit)
  }

  private object GarbageCollector {

    def collect(): Unit = emitted = emitted.filter(_.event.source.isValid)

  }

}
