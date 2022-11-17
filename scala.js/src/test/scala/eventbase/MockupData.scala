package eventbase

import meijs.eventbase.Database
import meijs.eventbase.structures.{
  AtomicEvent,
  CompositeEvent,
  CompositeExpression,
  Data
}
import meijs.modality.Modality
import meijs.modality.Modality.{Mouse, Voice}

object MockupData {
  val put: AtomicEvent    = MockUpVoiceEvent("put")
  val that: AtomicEvent   = MockUpVoiceEvent("that")
  val click1: AtomicEvent = MockUpMouseEvent("click1")
  val there: AtomicEvent  = MockUpVoiceEvent("there")
  val click2: AtomicEvent = MockUpMouseEvent("click2")

  val putThatThere: CompositeEvent = new CompositeEvent {
    val maybeName: Option[String] = Some("putThatThere")
    val expression: CompositeExpression =
      put `;` (that + click1) `;` (there | click2)
  }

  val sayHi: CompositeEvent = new CompositeEvent {
    override val maybeName: Option[String] = Some("say-hi")
    override val expression: CompositeExpression =
      Voice("say hi") + Mouse("click")
  }

  def defaultEvents: List[CompositeEvent] = List(sayHi, putThatThere)

  def fakeVoice(word: String): Unit = Database += (Data from Voice(word))

  def fakeClick(): Unit = Database += (Data from Mouse("click"))

  case class MockUpVoiceEvent(name: String) extends AtomicEvent {
    override val modality: Modality = Modality.Voice
  }

  case class MockUpMouseEvent(name: String) extends AtomicEvent {
    override val modality: Modality = Modality.Mouse
  }
}
