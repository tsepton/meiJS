package meijs.structures

import org.scalajs.dom

trait Fact {
  val validationTime: Long
  val emissionTime: Long = System.currentTimeMillis / 1000

  def validUntil: Long = emissionTime + validationTime
  def isValid: Boolean = validUntil < (System.currentTimeMillis / 1000)
}

case class Event(
    validationTime: Long,
    target: Option[dom.Node] = Option.empty
) extends Fact {}