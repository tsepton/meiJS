package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.structures.AtomicEvent

trait StateMachine {

  // This is a workaround so that the return type of these method is the type of the concrete instance
  // However it is not perfect, the compiler will not ensure that the return type conforms to what's declared
  // https://stackoverflow.com/questions/14729996/scala-implementing-method-with-return-type-of-concrete-instance
  type Self <: StateMachine

  def startState: State

  def endState: State

  def size: Int = states.length

  def states: List[State]
  def events: List[AtomicEvent]

  /** See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    */
  def overlay(that: Self): Self

  /** See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    */
  def permute(that: Self): Self

  /** See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    */
  def loop: Self

  /** See: "A domain-specific textual language for rapid prototyping of multimodal interactive systems"
    */
  def concatenate(that: Self): Self

}
