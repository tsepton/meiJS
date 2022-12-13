package meijs.modality

import meijs.eventbase.structures.AtomicEvent

trait Modality {

  def apply(name: String): AtomicEvent = {
    val m = this
    val n = name
    new AtomicEvent {
      override val name: String       = n
      override val modality: Modality = m
    }
  }

  override def toString: String = this.getClass.getName.split("\\$").last

}

object Modality {

  case object Keyboard extends Modality

  case object Mouse extends Modality

  case object Gaze extends Modality

  case object Hand extends Modality

  case object Voice extends Modality

}
