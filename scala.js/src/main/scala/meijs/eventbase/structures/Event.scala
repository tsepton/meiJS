package meijs.eventbase.structures

import scala.concurrent.duration._

trait Event {
  val validationDelay: FiniteDuration = 1.seconds // TODO
  val emissionTime: Long = System.currentTimeMillis / 1000

  def isValid: Boolean = validUntil > (System.currentTimeMillis / 1000)

  def validUntil: Long = emissionTime + validationDelay.toSeconds

  // Operators

  def `;`(e: Event): CompositeEvent = followedBy(e)

  def followedBy(e: Event): CompositeEvent = FollowedBy(this, e)

  def |(e: Event): CompositeEvent = or(e)

  def or(e: Event): CompositeEvent = Or(this, e)

  def +(e: Event): CompositeEvent = and(e)

  def and(e: Event): CompositeEvent = And(this, e)

  def *(): CompositeEvent = iteration

  def iteration: CompositeEvent = Iteration(this)
}

trait AtomicEvent extends Event

trait CompositeEvent extends Event
