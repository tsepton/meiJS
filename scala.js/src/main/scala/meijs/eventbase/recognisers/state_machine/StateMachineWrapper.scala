package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Database
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

case class StateMachineWrapper(
    event: CompositeEvent,
    f: CompositeEvent => StateMachine
) {
  private val machine: StateMachine = f(event)
  private val stateMuterInterval: SetIntervalHandle =
    js.timers.setInterval(50) {
      processNewEvents()
    }
  private var currentState: State    = machine.startState
  private var lastEmissionTime: Long = 0

  private def processNewEvents(): Unit = {
    val newEventsOrdered: List[Data] =
      Database.sortWith(_.emissionTime < _.emissionTime).collect {
        case e: AtomicEvent if e.emissionTime > lastEmissionTime => e
      }
    newEventsOrdered.lastOption.foreach(e => lastEmissionTime = e.emissionTime)
    newEventsOrdered.foreach {
      case e: AtomicEvent if currentState.events.contains(e) =>
        if (machine.endState == currentState.transitions(e)) Database += event
        else currentState = currentState.transitions(e)
    }
  }

}
