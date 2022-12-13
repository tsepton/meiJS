package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.recognisers.state_machine.State.StateIdentifier
import meijs.eventbase.structures.AtomicEvent

// TODO: This horrible code will be entirely re-written !
// With no data structure having a reference to itself !
// Will be immutable !

/** Represent a state which has events leading to other states
  *
  * @param _transitions : Event is an event leading to other state
  */
sealed case class State private (
    private var _transitions: Map[AtomicEvent, State],
    private var _stable: Boolean = true
)(implicit val stateIdentifier: StateIdentifier) {

  val id: Int = stateIdentifier.get

  def transitions: Map[AtomicEvent, State] = _transitions

  def stable: Boolean = _stable

  def setUnstable(): Unit = _stable = false

  def children: List[State] = transitions.values.toList

  def events: List[AtomicEvent] = transitions.keys.toList

  def eventsFromState(target: State): List[AtomicEvent] = transitions
    .filter { case (_, state) =>
      state == target
    }
    .keys
    .toList

  def eventFromName(name: String): Option[AtomicEvent] = transitions
    .find { case (event, _) =>
      event.name == name
    }
    .map(_._1)

  def put(event: AtomicEvent, state: State): Unit = _transitions =
    _transitions ++ Map(event -> state)

  override def toString: String                   =
    (if (transitions.nonEmpty)
       transitions.map { case (event, state) =>
         f"($id) -${event.name}-> (${state.id})"
       }.toList
     else List(f"($id)")).toString

  override def hashCode: Int = id

}

object State {

  private implicit val commonStateIdentifier: StateIdentifier = StateIdentifier()

  def initial: State = initial(commonStateIdentifier)

  def initial(identifier: StateIdentifier): State = {
    identifier.increment()
    State(_transitions = Map.empty)(identifier)
  }

  sealed case class StateIdentifier() {
    private var total = 0

    def get: Int = total

    def increment(): Unit = total += 1
  }
}
