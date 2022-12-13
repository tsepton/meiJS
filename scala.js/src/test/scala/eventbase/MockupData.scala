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

  val putMultipleTimes: CompositeEvent = new CompositeEvent {
    val name: String                    = "putMultipleTimes"
    val expression: CompositeExpression = put.*()
  }

  val putPlusThat: CompositeEvent = new CompositeEvent {
    val name: String                    = "putPlusThat"
    val expression: CompositeExpression = (put + that)
  }

  val putOrThat: CompositeEvent = new CompositeEvent {
    val name: String                    = "putOrThat"
    val expression: CompositeExpression = (put | that)
  }

  val putOrPut: CompositeEvent = new CompositeEvent {
    val name: String                    = "putOrPut"
    val expression: CompositeExpression = (put | put)
  }

  val putFollowedByThat: CompositeEvent = new CompositeEvent {
    val name: String                    = "putFollowedByThat"
    val expression: CompositeExpression = put `;` that
  }

  val selectThis: CompositeEvent = new CompositeEvent {
    override val name: String                    = "selectThis"
    override val expression: CompositeExpression =
      Voice("select") `;` Mouse("click").*()
  }

  val putThatThere: CompositeEvent = new CompositeEvent {
    val name: String                    = "putThatThere"
    val expression: CompositeExpression =
      put `;` (that + click1) `;` (there + click2)
  }

  val sayHi: CompositeEvent = new CompositeEvent {
    override val name: String                    = "sayHi"
    override val expression: CompositeExpression = Voice("say hi") + Mouse("click")
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
