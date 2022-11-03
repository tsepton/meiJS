package meijs.api.event_system

import meijs.factbase.FactBase
import meijs.factbase.structures.CompletedCommand

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

object MultimodalEventSystem {

  private val _emitted = new ListBuffer[EmittedCommand]()

  /** Set an interval which will collect and emit all completed MultimodalEvent in the fact base
    * @param ms: the time interval in milliseconds between each collect of events
    */
  def init(ms: Int): Unit = {
    js.timers.setInterval(ms) { handleEmission() }
    js.timers.setInterval(ms * 10) { GarbageCollector.collect() }
  }

  private def handleEmission(): Unit = {
    val commands = FactBase
      .collect { case e: CompletedCommand => e }
      .map(MultimodalEvent.from)
      .filterNot(_emitted.contains)
    _emitted ++= commands.map(EmittedCommand.from)
    commands.foreach(_.emit)
  }

  private object GarbageCollector {

    def collect(): Unit = MultimodalEventSystem._emitted.filter(c =>
      c.at > System.currentTimeMillis() - c.command.source.validUntil
    )

  }

}
