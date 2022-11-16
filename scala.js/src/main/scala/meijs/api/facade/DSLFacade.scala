package meijs.api.facade

import meijs.eventbase.structures.AtomicEvent
import meijs.modality.Modality

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

// TODO : read this for multiple module exports : https://www.scala-js.org/doc/project/module.html

@JSExportTopLevel("modality")
@JSExportAll
object ModalityFacade {

  def voice(n: String): EventFacade.JSAtomicEvent = genericMapper(n, Modality.Voice)

  def mouse(n: String): EventFacade.JSAtomicEvent = genericMapper(n, Modality.Mouse)

  def keyboard(n: String): EventFacade.JSAtomicEvent =
    genericMapper(n, Modality.Keyboard)

  def gaze(n: String): EventFacade.JSAtomicEvent = genericMapper(n, Modality.Gaze)

  def hand(n: String): EventFacade.JSAtomicEvent = genericMapper(n, Modality.Hand)

  private def genericMapper(n: String, m: Modality): AtomicEvent = new AtomicEvent {
    override val name: String       = n
    override val modality: Modality = m
  }
}

@JSExportTopLevel("event")
@JSExportAll
object EventFacade {
  import scala.language.implicitConversions

  class JSAtomicEvent(override val name: String, override val modality: Modality)
      extends AtomicEvent

  object JSAtomicEvent {
    implicit def from(atomicEvent: AtomicEvent): JSAtomicEvent =
      new JSAtomicEvent(atomicEvent.name, atomicEvent.modality)
  }

}
