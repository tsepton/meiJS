package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Database
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

case class StateMachineWrapper(
    event: CompositeEvent,
    f: CompositeEvent => StateMachine
) {
  val machine: StateMachine                         = f(event)
  private val stateMuterInterval: SetIntervalHandle = js.timers.setInterval(100) {
    processNewEvents()
  }
  private var occurrence: List[AtomicEvent]         = Nil
  private var currentState: State                   = machine.startState
  private var lastCheckForEmissionTime: Long        = 0
  // if no new events happen during that interval, it will trigger a reset
  private var timeoutInterval                       = setTimeoutInterval()
  private var stableTimeoutInterval                 = setTimeoutInterval()

  private def setTimeoutInterval(): SetIntervalHandle = js.timers.setInterval(5000) {
    reset()
  }

  // TODO : timer config - in the original paper, it is left for the end user to configure it
  private def setStableTimeoutInterval(state: State) = js.timers.setInterval(500) {
    assert(state.stable)
    currentState = state
  }

  private def clearTimeoutIntervals(): Unit = {
    js.timers.clearInterval(timeoutInterval)
    js.timers.clearInterval(stableTimeoutInterval)
  }

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
        clearTimeoutIntervals()
        occurrence = occurrence appended event
        currentState transitions e
      })
      .foreach(nextState => {
        if (nextState == machine.endState) {
          emit()
          reset()
        } else {
          if (!nextState.stable)
            stableTimeoutInterval = setStableTimeoutInterval(currentState)
          currentState = nextState
        }
        timeoutInterval = setTimeoutInterval()
      })
  }

  private def reset(): Unit = {
    currentState = machine.startState
    occurrence = Nil
  }

  private def emit(): Unit = Database += Data.from(this.event, occurrence)

}
