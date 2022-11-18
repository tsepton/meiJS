package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Database
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

// TODO see article - somes nodes are said to be unstable ! this represents the timing of the and operator
case class StateMachineWrapper(
    event: CompositeEvent,
    f: CompositeEvent => StateMachine
) {
  val machine: StateMachine = f(event)
  private val stateMuterInterval: SetIntervalHandle = js.timers.setInterval(100) {
    processNewEvents()
  }
  private val timeoutInterval: SetIntervalHandle = js.timers.setInterval(5000) {
    reset()
  }
  private var occurrence: List[AtomicEvent]  = Nil
  private var currentState: State            = machine.startState
  private var lastCheckForEmissionTime: Long = 0

  private def processNewEvents(): Unit = {
    val unprocessed: List[Data] =
      Database.sortWith(_.emissionTime < _.emissionTime).collect {
        case data: Data if data.emissionTime >= lastCheckForEmissionTime => data
      }
    if (unprocessed.nonEmpty) {
      lastCheckForEmissionTime = unprocessed.map(_.emissionTime).max
      unprocessed
        .map(_.event)
        .collect { case event: AtomicEvent => event }
        .foreach(tryUpdate)
    }
  }

  private def tryUpdate(event: AtomicEvent): Unit = {
    currentState.events
      .find(e => e.name == event.name)
      .map(e => {
        occurrence = occurrence appended event
        currentState.transitions(e)
      }) // get state from event
      .foreach(nextState => {
        if (nextState == machine.endState) {
          emit(occurrence)
          reset()
        } else currentState = nextState
      })
  }

  private def reset(): Unit = {
    currentState = machine.startState
    occurrence = Nil
  }

  private def emit(occurrence: List[AtomicEvent]): Unit =
    Database += Data.from(this.event, occurrence)

}
