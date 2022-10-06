package meijs.api.event_system

import meijs.FactBase
import meijs.structures.Event
import org.scalajs.dom
import org.scalajs.dom.{document}

import scala.scalajs.js

object MultimodalEventSystem {

  def init(): Unit = js.timers.setInterval(50) {
    for {
      event <- FactBase.collect { case e: Event => e }
      domEvent = new dom.Event("MultimodalEvent")
      _ = document.dispatchEvent(domEvent)
    } yield ()
  }

  //new CustomEvent().initCustomEvent('build', false, false, elem.dataset.time)


}
