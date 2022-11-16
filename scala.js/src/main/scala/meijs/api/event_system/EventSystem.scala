package meijs.api.event_system

import meijs.eventbase.structures.Data
import meijs.eventbase.{Database, Registry}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

object EventSystem {

  private val emitted = new ListBuffer[EmittedEvent]()

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
      .collect { case data: Data => JavascriptEvent from data }
      .filterNot(emitted.contains)
    emitted ++= commands.map(_.emit)
  }

  private object GarbageCollector {

    def collect(): Unit = EventSystem.this.emitted.filter(c =>
      c.at > System.currentTimeMillis() - c.event.source.validUntil
    )

  }

}
