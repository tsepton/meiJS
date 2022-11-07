package meijs.eventbase.structures

/** Represents a finite event emitted by a modality.
  */
trait Event {

  def `;`(e: Event): CompositeExpression = followedBy(e)

  def followedBy(e: Event): CompositeExpression = FollowedBy(this, e)

  def |(e: Event): CompositeExpression = or(e)

  def or(e: Event): CompositeExpression = Or(this, e)

  def +(e: Event): CompositeExpression = and(e)

  def and(e: Event): CompositeExpression = And(this, e)

  def *(): CompositeExpression = iteration

  def iteration: CompositeExpression = Iteration(this)

}

/** Event happening instantaneously
  *
  * eg: mouse click
  */
trait AtomicEvent extends Event {
  val name: String
}

/** Event composed of atomic event, happening over a period of time
  */
trait CompositeEvent extends Event {
  val maybeName: Option[String]
  val expression: CompositeExpression
}
