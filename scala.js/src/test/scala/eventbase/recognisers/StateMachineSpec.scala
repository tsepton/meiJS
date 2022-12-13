package eventbase.recognisers

import meijs.eventbase.Registry
import meijs.eventbase.recognisers.state_machine.{
  SMRecogniser,
  StateMachine,
  StateMachineWrapper
}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import scala.language.implicitConversions

/** Test suite for the state_machine package.
  */
class StateMachineSpec extends AnyFunSuite with BeforeAndAfter {

  import eventbase.MockupData._

  /** wrapper was written after - and I have no plan to update this code */
  implicit def fromWrapperToStateMachineDangerousConverter(
      wrapper: StateMachineWrapper
  ): StateMachine = wrapper.machine

  after {
    Registry.clean()
    SMRecogniser.clean()
  }

  test("Test the sync method from the SMRecogniser class") {
    Registry += putThatThere
    SMRecogniser.sync()
    assert(SMRecogniser.stateMachines.length == 1)
  }

  test(
    "Test the state method from a state machine instance"
  ) {
    Registry += putMultipleTimes
    Registry += putFollowedByThat
    SMRecogniser.sync()
    SMRecogniser.stateMachines.zipWithIndex.foreach { case (sm, index) =>
      assert(sm.states.length == sm.size)
      if (index == 0) assert(sm.states.length == 1)
      if (index == 1) assert(sm.states.length == 3)
    }
  }

  test("Ensure basic overlay operation is correct") {
    Registry += putOrThat
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 2)
      assert(sm.events.length == 2)
    })
  }

  test("Ensure overlay do not duplicate same event") {
    Registry += putOrPut
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 2)
      assert(sm.events.length == 1)
    })
  }

  test("Ensure basic concatenate operation is correct") {
    Registry += putFollowedByThat
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 3)
      assert(sm.events.length == 2)
    })
  }

  test("Ensure basic permute operation is correct") {
    Registry += putPlusThat
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 4)
      assert(sm.events.length == 2)
    })
  }

  test("Ensure permute operation set nodes as unstable for putThatThere") {
    Registry += putThatThere
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.states.count(!_.stable) == 4)
    })
  }

  test("Ensure permute operation set nodes as unstable for sayHi") {
    Registry += sayHi
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.states.count(!_.stable) == 2)
    })
  }

  test("Ensure basic loop operation is correct") {
    Registry += putMultipleTimes
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 1)
      assert(sm.events.length == 1)
    })
  }

  test("Ensure complex loop operation is correct") {
    Registry += selectThis
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 2)
      assert(sm.events.length == 2)
    })
  }

  test("Ensure put that there example is correct") {
    Registry += putThatThere
    SMRecogniser.sync()
    SMRecogniser.stateMachines.foreach(sm => {
      assert(sm.size == 8)
      assert(sm.events.length == 5)
    })
  }

}
