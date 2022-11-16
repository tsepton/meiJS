package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Database
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

case class StateMachineWrapper(
    event: CompositeEvent,
    f: CompositeEvent => StateMachine
) {
  // TODO add a timer
  // TODO see article - somes nodes are said to be unstable ! this represents the timing of the and operator
  private val machine: StateMachine = f(event)
  private val stateMuterInterval: SetIntervalHandle =
    js.timers.setInterval(50) {
      processNewEvents()
    }
  private var currentState: State    = machine.startState
  private var lastEmissionTime: Long = 0

  private def processNewEvents(): Unit = {
    val newDataOrdered: List[Data] =
      Database.sortWith(_.emissionTime < _.emissionTime).collect {
        case data: Data if data.emissionTime > lastEmissionTime => data
      }
    newDataOrdered.lastOption.foreach(e => lastEmissionTime = e.emissionTime)
    newDataOrdered.map(_.event).foreach {
      // TODO: speech.put =>
      //    if (currentState.event.modality == speech && currentState.event.semantic == put) then updating state
      // TODO: put => if (currentState.event.semantic == put) then updating state
      // Event trait has to specify its Modality
      case e: AtomicEvent if currentState.events.contains(e) =>
        if (machine.endState == currentState.transitions(e))
          Database += Data from event
        else currentState = currentState.transitions(e)
    }
  }

}
