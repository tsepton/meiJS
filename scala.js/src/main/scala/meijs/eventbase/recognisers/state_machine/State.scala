package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, Event}

/** Represent a state which has events leading to other states
  *
  * @param _transitions : Event is an event leading to other state
  */
case class State private (private var _transitions: Map[Event, State])(implicit
    val stateIdentifier: StateIdentifier
) {

  val id: Int = stateIdentifier.get

  def transitions: Map[Event, State] = _transitions

  def children: List[State] = transitions.values.toList

  def events: List[Event] = transitions.keys.toList

  def eventsFromState(target: State): List[Event] = transitions
    .filter { case (_, state) =>
      state == target
    }
    .keys
    .toList

  def put(event: Event, state: State): Unit = _transitions =
    _transitions ++ Map(event -> state)

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

}

case object State {

  private implicit val commonStateIdentifier: StateIdentifier = StateIdentifier()

  def initial: State = initial(commonStateIdentifier)

  def initial(identifier: StateIdentifier): State = {
    identifier.increment()
    State(_transitions = Map.empty)(identifier)
  }
}

case class StateIdentifier() {
  private var total = 0

  def get: Int = total

  def increment(): Unit = total += 1
}
