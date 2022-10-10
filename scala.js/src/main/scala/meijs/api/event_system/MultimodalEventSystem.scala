package meijs.api.event_system

import meijs.FactBase
import meijs.structures.CompletedCommand

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.scalajs.js

object MultimodalEventSystem {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val _emitted = new ListBuffer[EmittedCommand]()

  /** Set an interval which will collect and emit all completed MultimodalEvent in the fact base
    * @param ms: the time interval in milliseconds between each collect of events
    */
  def init(ms: Int = 50): Unit = {
    js.timers.setInterval(ms) { handleEmission() }
    js.timers.setInterval(ms*10) { GarbageCollector.collect() }
  }

  private def handleEmission(): Unit = {
    val commands = FactBase
      .collect { case e: CompletedCommand => e }
      .map(MultimodalEvent.from)
      .filterNot(_emitted.contains)
    Future { commands.foreach(_.emit) }
    _emitted += commands
  }

  private object GarbageCollector {

    def collect(): Unit = MultimodalEventSystem._emitted.filter(c =>
      c.at > System.currentTimeMillis() - c.command.source.validUntil
    )

  }

}
