package meijs.eventbase.structures

import scala.concurrent.duration._
import scala.language.implicitConversions

/** Represents an event occurrence.
  * Therefore, it is used inside the Database as the representation of an event that happened.
  */
trait Data {
  val validationDelay: FiniteDuration = 1.seconds // TODO

  val emissionTime: Long = System.currentTimeMillis / 1000

  def isValid: Boolean = validUntil > (System.currentTimeMillis / 1000)

  def validUntil: Long = emissionTime + validationDelay.toSeconds

  def event: Event

  override def toString: String = f"""Data with event: $event
  |   emissionTime: $emissionTime
  |   validUntil: $validUntil""".stripMargin
}

case object Data {
  def from(e: Event): Data = new Data {
    def event: Event = e
  }

  def from(events: List[Event]): List[Data] = events map from
}
