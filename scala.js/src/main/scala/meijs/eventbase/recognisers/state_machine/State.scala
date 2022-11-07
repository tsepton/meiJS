package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Event}

import scala.collection.mutable

/** Represent a state which has events leading to other states
  *
  * Note: Using a mutable Map is a deliberate choice
  *
  * @param transitions : Event is an event leading to other state
  */
case class State private (transitions: mutable.Map[Event, State])(implicit
    val stateIdentifier: StateIdentifier
) {

  def children: List[State] = transitions.values.toList

  def events: List[Event] = transitions.keys.toList

  def findEventsFromState(target: State): List[Event] = transitions
    .filter { case (_, state) =>
      state == target
    }
    .keys
    .toList

  override def toString: String = {
    transitions
      .map {
        case (event: AtomicEvent, state) =>
          f"$id -${event.name}-> ${state.id}"
        case (event: CompositeEvent, state) =>
          f"$id -${event.maybeName.getOrElse("Unnamed event")}-> ${state.id}"
      }
      .toList
      .toString
  }

  def id: Int = stateIdentifier.get

}

case object State {
  def initial(implicit identifier: StateIdentifier): State = {
    identifier.increment()
    State(transitions = mutable.Map.empty)
  }
}

case class StateIdentifier() {
  private var total = 0

  def get: Int = total

  def increment(): Unit = total += 1
}
