package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Database
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

case class StateMachineWrapper(
    event: CompositeEvent,
    f: CompositeEvent => StateMachine
) {
  val machine: StateMachine = f(event)
  private val stateMuterInterval: SetIntervalHandle = js.timers.setInterval(50) {
    processNewEvents()
  }
  private val timeoutInterval: SetIntervalHandle = js.timers.setInterval(1000) {
    currentState = machine.startState
  }
  private var currentState: State = machine.startState
  // TODO see article - somes nodes are said to be unstable ! this represents the timing of the and operator
  private var lastCheckForEmissionTime: Long = 0

  // TODO : refactor this shitty and unmaintainable method
  private def processNewEvents(): Unit = {
    val newDataOrdered: List[Data] =
      Database.sortWith(_.emissionTime < _.emissionTime).collect {
        case data: Data if data.emissionTime >= lastCheckForEmissionTime => data
      }
    if (newDataOrdered.nonEmpty) {
      lastCheckForEmissionTime = newDataOrdered.map(_.emissionTime).max
      newDataOrdered
        .map(_.event)
        .collect { case event: AtomicEvent => event }
        .foreach { event =>
          val maybeNextState = currentState.events.find(e => e.name == event.name)
          //.filter(event => if (maybeModality.isDefined) currentState.events.exists(e => e.modality == maybeModality.get) else true) TODO
          if (maybeNextState.isDefined) {
            val nextState = maybeNextState.map(currentState.transitions).get
            if (nextState == machine.endState) {
              Database += Data from this.event
              currentState = machine.startState
            } else currentState = nextState
          }
        }
    }
  }

}
