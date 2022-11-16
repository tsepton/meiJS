package meijs.modality.dom

import meijs.eventbase.Database
import meijs.eventbase.structures.Data
import meijs.modality.ModalityInterpreter
import org.scalajs.dom.{KeyboardEvent, MouseEvent}
import org.scalajs.{dom => domjs}

import scala.language.implicitConversions
import scala.scalajs.js

object Dom extends ModalityInterpreter {

  private lazy val events: List[String] =
    List(js.Object.keys(domjs.document), js.Object.keys(domjs.window)).flatten
      .filter(_.startsWith("on"))
      .map(str => str.slice(2, str.length + 1))

  def init(): Unit = events.foreach(domjs.window.addEventListener(_, registerEvent))

  private def registerEvent(event: domjs.Event): Unit = {
    val maybeDomEvent = event match {
      case x if x.isInstanceOf[MouseEvent] =>
        Some(DomEvent fromMouseEvent x.asInstanceOf[MouseEvent])
      case x if x.isInstanceOf[KeyboardEvent] =>
        Some(DomEvent fromKeyboardEvent x.asInstanceOf[KeyboardEvent])
      case _ => None
    }
    maybeDomEvent.foreach(Database += Data from _)
  }

}
