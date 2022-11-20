package meijs.api.facade

import meijs.eventbase.structures.{AtomicEvent, CompositeExpression, Event}
import meijs.modality.Modality

import scala.scalajs.js.annotation.{JSExport, JSExportAll, JSExportTopLevel}

// TODO : read this for multiple module exports : https://www.scala-js.org/doc/project/module.html

// TODO : a javascript wrapper for the dsl
// Work in progress...
@JSExportTopLevel("modality")
@JSExportAll
object ModalityFacade {

  def voice(n: String): AtomicEvent = genericMapper(n, Modality.Voice)

  def mouse(n: String): AtomicEvent = genericMapper(n, Modality.Mouse)

  def keyboard(n: String): AtomicEvent =
    genericMapper(n, Modality.Keyboard)

  def gaze(n: String): AtomicEvent = genericMapper(n, Modality.Gaze)

  def hand(n: String): AtomicEvent = genericMapper(n, Modality.Hand)

  private def genericMapper(n: String, m: Modality): AtomicEvent = new AtomicEvent {
    override val name: String       = n
    override val modality: Modality = m
  }
}

@JSExportTopLevel("event")
object EventFacade {

  @JSExport
  def or(e1: Event, e2: Event): CompositeExpression = e1 | e2

}
