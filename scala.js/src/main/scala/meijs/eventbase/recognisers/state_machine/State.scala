package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.structures.Event

import scala.collection.mutable

/** Represent a node which has events leading to other nodes
  *
  * Note: Using a mutable Map is a deliberate choice
  *
  * @param transitions : Event is an event leading to other node (State)
  */
case class State private (transitions: mutable.Map[Event, State]) {

  def childrenStates: List[State] = transitions.values.toList

  def findEventsFromState(target: State): List[Event] = transitions
    .filter { case (_, state) =>
      state == target
    }
    .keys
    .toList
}

case object State {
  def initial: State = State(transitions = mutable.Map.empty)
}
