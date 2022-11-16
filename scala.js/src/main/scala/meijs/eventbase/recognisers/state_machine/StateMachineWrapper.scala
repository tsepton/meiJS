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
  private val stateMuterInterval: SetIntervalHandle = js.timers.setInterval(50) {
    processNewEvents()
  }
  private val timeoutInterval: SetIntervalHandle = js.timers.setInterval(2000) {
    currentState = machine.startState
  }
  // TODO see article - somes nodes are said to be unstable ! this represents the timing of the and operator
  private var currentState: State    = machine.startState
  private var lastEmissionTime: Long = 0

  private def processNewEvents(): Unit = {
    val newDataOrdered: List[Data] =
      Database.sortWith(_.emissionTime < _.emissionTime).collect {
        case data: Data if data.emissionTime > lastEmissionTime => data
      }
    newDataOrdered.lastOption.foreach(e => lastEmissionTime = e.emissionTime)
    newDataOrdered
      .map(_.event)
      .collect { case event: AtomicEvent => event }
      .foreach { e =>
        val maybeNextState = currentState.events.find(e => e.name == e.name)
        //.filter(event => if (maybeModality.isDefined) currentState.events.exists(e => e.modality == maybeModality.get) else true) TODO
        if (maybeNextState.isDefined) {
          val nextState = maybeNextState.map(currentState.transitions).get
          if (nextState == machine.endState) {
            Database += Data from event
          } else currentState = nextState
        }
      }
    // TODO: speech.put =>
    //    if (currentState.event.modality == speech && currentState.event.semantic == put) then updating state
    // TODO: put => if (currentState.event.semantic == put) then updating state
  }

}
