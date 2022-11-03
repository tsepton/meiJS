package meijs.modality.dom

import meijs.eventbase.EventBase
import meijs.modality.Modality
import org.scalajs.{dom ⇒ domjs}

import scala.language.implicitConversions
import scala.scalajs.js

object Dom extends Modality {

  private lazy val events: List[String] =
    List(js.Object.keys(domjs.document), js.Object.keys(domjs.window)).flatten
      .filter(_.startsWith("on"))
      .map(str ⇒ str.slice(2, str.length + 1))

  def init(): Unit =
    events.foreach(domjs.window.addEventListener(_, registerEvent))

  private def registerEvent(event: domjs.Event): Unit = {
    EventBase += DomEvent.fromJSEvent(event)
  }
}
