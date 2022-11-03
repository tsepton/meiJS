package meijs.modality.dom

import meijs.eventbase.EventBase
import meijs.modality.Modality
import org.scalajs.dom._

import scala.language.implicitConversions
import scala.scalajs.js

object Dom extends Modality {

  private lazy val events: List[String] =
    List(js.Object.keys(document), js.Object.keys(window)).flatten
      .filter(_.startsWith("on"))
      .map(str ⇒ str.slice(2, str.length + 1))

  def init(): Unit = events.foreach(window.addEventListener(_, registerEvent))

  private def registerEvent(event: Event): Unit = {
    EventBase += DomEvent.fromJSEvent(event)
  }

}
