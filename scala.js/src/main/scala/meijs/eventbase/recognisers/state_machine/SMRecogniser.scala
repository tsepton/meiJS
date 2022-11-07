package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Registry
import meijs.eventbase.recognisers.Recogniser
import meijs.eventbase.structures._

import scala.collection.mutable.ListBuffer

object SMRecogniser extends Recogniser {

  private val _registeredEvents: ListBuffer[Event]            = ListBuffer()
  private val _stateMachines: ListBuffer[StateMachineMutable] = ListBuffer()

  def stateMachines: List[StateMachineMutable] = _stateMachines.toList

  def init(): Unit = {
    sync()
  }

  /** Synchronise this internal state with the Registry registered events.
    * Will create a state machine per registered event
    */
  def sync(): Unit = {
    _registeredEvents ++= Registry.registry.filterNot(_registeredEvents contains _)
    _stateMachines ++= _registeredEvents.map(createStateMachine(_))
  }

  /** Synchronise this internal state with the Registry registered events.
    * Will create a state machine per registered event
    */
  def clean(): Unit = {
    _registeredEvents.clear()
    _stateMachines.clear()
  }

  /** Translate an Event into a state machine
    *
    * FIXME: actually not tail recursive
    * FIXME: Make a Factory for the statemachine implementation
    * https://refactoring.guru/design-patterns/abstract-factory
    *
    * @param event
    * @param smAcc
    * @return
    */
  // @tailrec
  private def createStateMachine(
      event: Event,
      smAcc: StateMachineMutable = StateMachineMutable.initial
  ): StateMachineMutable = {
    event match {
      case e: AtomicEvent => StateMachineMutable.initWithSimpleEvent(e)
      case e: CompositeEvent =>
        e.expression match {
          case exp: Iteration => createStateMachine(exp.left, smAcc).loop
          case exp: And =>
            createStateMachine(exp.left, smAcc) permute
              createStateMachine(exp.right, smAcc)
          case exp: Or =>
            createStateMachine(exp.left, smAcc) overlay
              createStateMachine(exp.right, smAcc)
          case exp: FollowedBy =>
            createStateMachine(exp.left, smAcc) concatenate
              createStateMachine(exp.right, smAcc)
        }
    }
  }

}
