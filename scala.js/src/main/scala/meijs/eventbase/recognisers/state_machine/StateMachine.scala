package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.structures.Event

class StateMachine private (var startState: State = State.initial) {

  private type Path = List[Event]

  // FIXME: find a way to compute it instead of having a var
  private var endState: State = startState

  /** All sm intermediate states are added to this, with the startState of this becoming the state leading to these
    * intermediate states, while the endState of sm become replaced by the endState of this. This leads to an overlay.
    * See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    *
    * BEWARE: sm is not untouched, it is altered and integrated into this
    *
    * @param sm : The StateMachine to overlay with this
    * @return
    */
  def overlay(sm: StateMachine): StateMachine = {
    // Adding sm intermediate states to this.startingState
    startState.transitions ++ sm.startState.transitions
    // Updating sm.endingState with this.endingState
    sm.findConnectedStates(to = sm.endState)
      .map(state => (state, state.findEventsFromState(sm.endState)))
      .foreach { case (state, events) =>
        events.foreach(state.transitions.put(_, endState))
      }
    this
  }

  private def findConnectedStates(to: State): List[State] =
    states.filter(_.transitions.values.toList.contains(endState))

  private def states: List[State] = {
    val allStates =
      (List(startState) ++ startState.transitions.values.toList).distinct
    (for {
      state <- allStates
      children = state.childrenStates
      unseenStates = children.filter(allStates.contains)
    } yield allStates.appendedAll(unseenStates)).flatten.distinct
  }

  /** @param sm : The StateMachine to do the permutation with this
    * @return this
    */
  def permute(sm: StateMachine): StateMachine = {
    val paths: List[Path] = allPaths
      .map(path => sm.allPaths.flatMap(smPath => path ++ smPath))
      .map(_.sortWith { (event1, event2) =>
        event1.toString.length > event2.toString.length
      })
      .distinct
      .permutations
      .toList
      .flatten
    for (path <- paths) {
      var from: State = startState
      path.zipWithIndex.foreach {
        case (event, i) if i + 1 == path.length => update(from, event, endState)
        case (event, _)                         => from = update(from, event)
      }
    }
    this
  }

  private def allPaths: List[Path] = {

    trait Transition {
      def state: State // current state
    }
    case class Root(state: State) extends Transition
    case class Branch(state: State, eventFromParent: Event) extends Transition

    def visitState(
        transition: Transition,
        visited: List[Transition] = Nil
    ): List[List[Transition]] =
      if (visited.map(_.state).contains(transition.state))
        List(visited :+ transition)
      else {
        val neighbours: List[Transition] = transition.state.transitions.map {
          case (event, target) => Branch(target, event)
        }.toList filterNot visited.contains
        neighbours.map(visitState(_, transition +: visited).flatten)
      }

    visitState(Root(startState)).map(_.map {
      case Branch(_, eventFromParent) => Some(eventFromParent)
      case _                          => Option.empty
    }.filter(_.isDefined).map(_.get))

  }

  /** Update `this` state machine by adding a transition to the State corresponding to `from`
    *
    * If (!states.contains(from)) `this` remains untouched
    *
    * @param from   : The state on which a new transition should be added
    * @param event  : The event leading to the transition
    * @param target : The state on which the event should lead to
    * @return the created State
    */
  private def update(
      from: State,
      event: Event,
      target: State = State.initial
  ): State = {
    states.find(state => state == from) match {
      case None => ()
      case Some(from) =>
        from.transitions.put(event, target)
        endState = target
    }
    target
  }

  /** The endState of this becomes replaced by its startState. This leads to a loop.
    * See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    *
    * BEWARE: sm is not untouched
    *
    * @return `this`
    */
  def loop: StateMachine = concatenate(this)

  /** The endState of this becomes replaced by the startState of sm. This leads to a concatenation of these events.
    * See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    *
    * BEWARE: sm is not untouched, it is altered and integrated into this
    *
    * @param sm : The StateMachine that will replace the ending state of this
    * @return `this`
    */
  def concatenate(sm: StateMachine): StateMachine = {
    findConnectedStates(to = endState)
      .map(state => (state, state.findEventsFromState(endState)))
      .foreach { case (state, events) =>
        events.foreach(state.transitions.put(_, sm.startState))
      }
    endState = sm.endState
    this
  }

}

case object StateMachine {
  def initWithSimpleEvent(event: Event): StateMachine = {
    val sm = initial
    sm.update(sm.endState, event)
    sm
  }

  def initial: StateMachine = new StateMachine()
}
