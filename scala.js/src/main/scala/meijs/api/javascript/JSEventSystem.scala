package meijs.api.javascript

import meijs.eventbase.structures.{Data, Event}
import meijs.eventbase.{Database, Registry}
import org.scalajs.dom.console

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("eventSystem")
object JSEventSystem {

  import js.JSConverters._
  import scala.language.implicitConversions

  /** Note: This allows scalaJS devs to use this api more easily */
  implicit def fromScalaArray[A](iterable: Iterable[A]): js.Array[A] =
    iterable.toJSArray

  type Callback = js.Function1[JSEvent, Unit]

  private val callbacks: mutable.Map[Event, js.Array[Callback]] = mutable.Map.empty
  private var emitted: List[JSEmittedEvent]                     = Nil

  private case class JSEmittedEvent(event: JSEvent, original: Data) {
    val at: Long = System.currentTimeMillis() / 1000
  }

  private object JSEmittedEvent {
    def from(event: JSEvent, data: Data): JSEmittedEvent =
      new JSEmittedEvent(event, data)
  }

  /** Set an interval which will collect and emit all completed MultimodalEvent in the fact base
    *
    * @param ms : the time interval in milliseconds between each collect of events
    */
  def init(ms: Int): Unit = {
    js.timers.setInterval(ms) {
      handleEmission()
    }
    js.timers.setInterval(ms * 10)(GarbageCollector.collect())
  }

  /** Register an event and the callback associated with */
  @JSExport("subscribe")
  def subscribe[A <: Event](event: Event, f: Callback): Unit = {
    Registry += event
    callbacks.put(event, callbacks.getOrElse(event, js.Array()) append f)
  }

  private def handleEmission(): Unit = {
    val dataToProcess: List[Data] = Database
      .filter(data => Registry.list contains data.event)
      .filterNot(data => emitted.map(_.event.source).contains(data))

    def emit(e: JSEvent): Unit = callbacks
      .find { case (event, _) => event.name == e.source.name }
      .map(_._2)
      .getOrElse(js.Array())
      .foreach(f => f(e))

    dataToProcess
      .collect { case data: Data => (JSEvent from data, data) }
      .map {
        case (event, data) => {
          emit(event)
          JSEmittedEvent.from(event, data)
        }
      }
  }

  private object GarbageCollector {
    def collect(): Unit = emitted = emitted.filter(_.event.isValid)
  }
}
