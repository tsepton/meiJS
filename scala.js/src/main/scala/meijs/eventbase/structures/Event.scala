package meijs.eventbase.structures

import scala.concurrent.duration._

trait Event {
  // FIXME: Event doesn't have these properties,
  // only events added inside the event base have these
  /////////////////////
  val validationDelay: FiniteDuration = 1.seconds // TODO

  val emissionTime: Long = System.currentTimeMillis / 1000

  def isValid: Boolean = validUntil > (System.currentTimeMillis / 1000)

  def validUntil: Long = emissionTime + validationDelay.toSeconds
  /////////////////////

  def `;`(e: Event): CompositeExpression = followedBy(e)

  def followedBy(e: Event): CompositeExpression = FollowedBy(this, e)

  def |(e: Event): CompositeExpression = or(e)

  def or(e: Event): CompositeExpression = Or(this, e)

  def +(e: Event): CompositeExpression = and(e)

  def and(e: Event): CompositeExpression = And(this, e)

  def *(): CompositeExpression = iteration

  def iteration: CompositeExpression = Iteration(this)

}

trait AtomicEvent extends Event {
  val name: String
}

trait CompositeEvent extends Event {
  val maybeName: Option[String]
  val expression: CompositeExpression
}
