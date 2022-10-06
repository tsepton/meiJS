package meijs.structures

import org.scalajs.dom

import scala.concurrent.duration.{DurationInt, FiniteDuration}

sealed trait Fact {
  val validationDelay: FiniteDuration
  val emissionTime: Long = System.currentTimeMillis / 1000

  def validUntil: Long = emissionTime + validationDelay.toSeconds
  def isValid: Boolean = validUntil < (System.currentTimeMillis / 1000)
}

/**
 * This represents a fact that should be communicated to the application
 *
 * !!!! This is not to be confused with org.scalajs.dom.Event
 *
 * FIXME: Find a proper name
 *
 * @param validationDelay
 * @param target
 */
case class CompletedCommand(
    validationDelay: FiniteDuration = 1.seconds,
    target: Option[dom.Node] = Option.empty
) extends Fact {}
