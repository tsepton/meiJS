package meijs.eventbase.structures

import meijs.modality.Modality

/** Represents a finite event emitted by a modality.
  */
trait Event {

  /** Must be unique ! */
  val name: String

  def `;`(e: Event): CompositeExpression = followedBy(e)

  def followedBy(e: Event): CompositeExpression = FollowedBy(this, e)

  def |(e: Event): CompositeExpression = or(e)

  def or(e: Event): CompositeExpression = Or(this, e)

  def +(e: Event): CompositeExpression = and(e)

  def and(e: Event): CompositeExpression = And(this, e)

  def *(): CompositeExpression = iteration

  def iteration: CompositeExpression = Iteration(this)

  override def equals(obj: Any): Boolean = obj match {
    case a: Event => a.name == this.name
    case _        => false
  }

  override def toString: String = name

}

/** Event happening instantaneously
  *
  * eg: mouse click
  */
trait AtomicEvent extends Event {
  val modality: Modality
}

/** Description of an event composed of atomic events, happening over a period of time */
trait CompositeEvent extends Event {
  val expression: CompositeExpression
}
