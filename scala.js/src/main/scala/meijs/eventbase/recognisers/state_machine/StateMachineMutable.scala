package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.recognisers.state_machine.StateMachineMutable.{
  Branch,
  Root,
  visitState
}
import meijs.eventbase.structures.AtomicEvent

class StateMachineMutable private () extends StateMachine {

  type Self         = StateMachineMutable
  private type Path = List[AtomicEvent]

  var startState: State = State.initial
  var endState: State   = startState

  def events: List[AtomicEvent] =
    visitState(Root(startState)).flatten.flatMap(_.state.events).distinct

  /** All that intermediate states are added to this, with the startState of this becoming the state leading to these
    * intermediate states, while the endState of that become replaced by the endState of this. This leads to an overlay.
    * See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    *
    * BEWARE: that is not untouched, it is altered and integrated into this
    *
    * @param that : The StateMachine to overlay with this
    * @return
    */
  def overlay(that: StateMachineMutable): StateMachineMutable = if (nonEmpty) {
    // Adding that intermediate states to this.startingState
    that.startState.transitions.foreach { case (event, state) =>
      if (that.endState == state) startState.put(event, this.endState)
      else startState.put(event, state)
    }
    // Updating that.endingState with this.endingState
    that
      .connectedStates(to = that.endState)
      .map(state => (state, state.eventsFromState(that.endState)))
      .foreach { case (state, events) =>
        events.foreach(state.put(_, endState))
      }
    this
  } else that

  /** A new state machine is returned which was built using all possible event permutations from this and that.
    * See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    *
    * @param that : The StateMachine to do the event permutations with this
    * @return this
    */
  def permute(that: StateMachineMutable): StateMachineMutable = {
    val paths: List[Path] = (for {
      thisEvents <- allPaths
      thatEvents <- that.allPaths
      sorted      = (thisEvents ++ thatEvents)
                      .sortWith { (event1, event2) =>
                        event1.toString.length > event2.toString.length
                      }
                      .distinct
                      .permutations
                      .toList
    } yield sorted).flatten
    paths
      .map {
        _.zipWithIndex
          .map {
            case (atomic, 0) => StateMachineMutable initWithSimpleEvent atomic
            case (atomic, _) =>
              val stateMachine = StateMachineMutable initWithSimpleEvent atomic
              stateMachine.startState.setUnstable()
              stateMachine
          }
          .foldLeft(StateMachineMutable.initial) { case (acc, sm) =>
            acc concatenate sm
          }
      }
      .foldLeft(StateMachineMutable.initial) { case (acc, sm) =>
        acc overlay sm
      }
  }

  /** The endState of this becomes replaced by its startState. This leads to a loop.
    * See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    *
    * @return `this`
    */
  def loop: StateMachineMutable = concatenate(this)

  /** The endState of this becomes replaced by the startState of that. This leads to a concatenation of these events.
    * See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    *
    * BEWARE: that is not untouched, it is altered and integrated into this
    *
    * @param that : The StateMachine that will replace the ending state of this
    * @return `this`
    */
  def concatenate(that: StateMachineMutable): StateMachineMutable = {
    if (isEmpty) that
    else {
      connectedStates(to = endState)
        .map(state => (state, state.eventsFromState(endState)))
        .foreach { case (state, events) =>
          events.foreach(state.put(_, that.startState))
        }
      endState = that.endState
      this
    }
  }

  def isEmpty: Boolean = startState.events.isEmpty

  override def toString: String = states.map(_.toString).toString

  def states: List[State] =
    visitState(Root(startState)).flatten.map(_.state).distinct

  private def connectedStates(to: State): List[State] =
    states.filter(_.children.contains(to))

  private def allPaths: List[Path] = visitState(Root(startState)).map(_.map {
    case Branch(_, eventFromParent) => Some(eventFromParent)
    case _                          => Option.empty
  }.filter(_.isDefined).map(_.get))

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
      event: AtomicEvent,
      target: State = State.initial
  ): State = {
    states.find(state => state == from) match {
      case None       => ()
      case Some(from) =>
        from.put(event, target)
        endState = target
    }
    target
  }

}

case object StateMachineMutable {
  def initWithSimpleEvent(event: AtomicEvent): StateMachineMutable = {
    val sm = initial
    sm.update(sm.endState, event)
    sm
  }

  def initial: StateMachineMutable = new StateMachineMutable()

  private def visitState(
      transition: Transition,
      visited: List[Transition] = Nil
  ): List[List[Transition]] = if (visited.map(_.state).contains(transition.state))
    List(visited)
  else {
    val neighbours: List[Branch] = transition.state.transitions.map {
      case (event, target) => Branch(target, event)
    }.toList filterNot visited.contains
    if (neighbours.isEmpty) List(visited :+ transition)
    else neighbours.map(visitState(_, visited :+ transition).flatten)
  }

  private trait Transition {
    def state: State // current state
  }

  private case class Root(state: State) extends Transition

  private case class Branch(state: State, eventFromParent: AtomicEvent)
      extends Transition
}
