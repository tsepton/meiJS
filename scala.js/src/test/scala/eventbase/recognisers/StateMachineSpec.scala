package eventbase.recognisers

import meijs.eventbase.Registry
import meijs.eventbase.structures.{
  AtomicEvent,
  CompositeEvent,
  CompositeExpression
}
import org.scalatest.funsuite.AnyFunSuite

class StateMachineSpec extends AnyFunSuite {

  val put: AtomicEvent = MockUpAtomicEvent("put")
  val that: AtomicEvent = MockUpAtomicEvent("that")
  val click1: AtomicEvent = MockUpAtomicEvent("click")
  val there: AtomicEvent = MockUpAtomicEvent("there")
  val click2: AtomicEvent = MockUpAtomicEvent("click")
  val putThatThere = new CompositeEvent {
    val maybeName: Option[String] = Some("putThatThere")
    val expression: CompositeExpression =
      put `;` (that + click1) `;` (there | click2)
  }

  case class MockUpAtomicEvent(name: String) extends AtomicEvent

  test("Registering simple command") {
    Registry += putThatThere
    assert(Registry.registry.length == 1)
  }

}
