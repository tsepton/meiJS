package meijs.api.event_system

import meijs.FactBase
import meijs.structures.CompletedCommand
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js

object MultimodalEventSystem {

  def init(): Unit = js.timers.setInterval(50) {
    for {
      event <- FactBase.collect { case e: CompletedCommand => e }
      domEvent = MultimodalEvent.from(event)
      _ = document.dispatchEvent(domEvent)
    } yield ()
  }

}
