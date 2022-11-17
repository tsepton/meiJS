package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Database
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Data}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

// TODO see article - somes nodes are said to be unstable ! this represents the timing of the and operator
// TODO : refactor this shitty and unmaintainable method
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
        .foreach { event =>
          // TODO unstable node -> fallback to previous stable node
          val maybeNextState = currentState.events.find(e => e.name == event.name)
          //.filter(event => if (maybeModality.isDefined) currentState.events.exists(e => e.modality == maybeModality.get) else true) TODO
          if (maybeNextState.isDefined) {
            val nextState = maybeNextState.map(currentState.transitions).get
            if (nextState == machine.endState) {
              emit()
              reset()
            } else currentState = nextState
          }
        }
    }
  }

  private def reset(): Unit = currentState = machine.startState

  private def emit(): Unit = Database += Data from this.event

}
