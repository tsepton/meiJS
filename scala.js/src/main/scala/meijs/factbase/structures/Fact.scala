package meijs.factbase.structures

import org.scalajs.dom

import scala.concurrent.duration.{DurationInt, FiniteDuration}

sealed trait Fact {
  val validationDelay: FiniteDuration
  val emissionTime: Long = System.currentTimeMillis / 1000

  def isValid: Boolean = validUntil > (System.currentTimeMillis / 1000)
  def validUntil: Long = emissionTime + validationDelay.toSeconds
}

/** This represents a fact that should be communicated to the application
  *
  * @param validationDelay
  * @param target
  */
case class CompletedCommand(
    validationDelay: FiniteDuration = 1.seconds,
    target: Option[dom.Node] = Option.empty
) extends Fact {
  // TODO - once we have defined what a command is...
  override def toString: String = "???"
}
