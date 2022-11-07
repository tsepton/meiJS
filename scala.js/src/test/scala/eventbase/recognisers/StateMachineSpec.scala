package eventbase.recognisers

import meijs.eventbase.Registry
import meijs.eventbase.recognisers.state_machine.SMRecogniser
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, CompositeExpression}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

/** Test suite for the state_machine package.
  */
class StateMachineSpec extends AnyFunSuite with BeforeAndAfter {

  val put: AtomicEvent    = MockUpAtomicEvent("put")
  val that: AtomicEvent   = MockUpAtomicEvent("that")
  val click1: AtomicEvent = MockUpAtomicEvent("click")
  val there: AtomicEvent  = MockUpAtomicEvent("there")
  val click2: AtomicEvent = MockUpAtomicEvent("click")

  val putThatThere: CompositeEvent = new CompositeEvent {
    val maybeName: Option[String] = Some("putThatThere")
    val expression: CompositeExpression =
      put `;` (that + click1) `;` (there | click2)
  }

  case class MockUpAtomicEvent(name: String) extends AtomicEvent

  after {
    Registry.clean()
    SMRecogniser.clean()
  }

  test("Test the sync method from the SMRecogniser class") {
    Registry += putThatThere
    SMRecogniser.sync()
    assert(SMRecogniser.stateMachines.length == 1)
  }

  test("Test the state method from a state machine instance") {
    Registry += put.*
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach { sm =>
      assert(sm.states.length == sm.size)
      assert(sm.states.length == 1)
    }
  }

  test("Ensure overlay operation is correct") {
    Registry += put | that
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      println(sm.startState)
      assert(sm.size == 2)
      // TODO verify everything else...
    })
  }

  test("Ensure concatenate operation is correct") {
    Registry += put `;` that
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      println(sm.startState)
      assert(sm.size == 3)
      // TODO verify everything else...
    })
  }

  test("Ensure permute operation is correct") {
    Registry += put + that
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 4)
      // TODO verify everything else...
    })
  }

  test("Ensure loop operation is correct") {
    Registry += (put + that).*
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 3)
      // TODO verify everything else...
    })
  }

}
