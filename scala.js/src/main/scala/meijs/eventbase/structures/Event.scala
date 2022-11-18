package meijs.eventbase.structures

import meijs.modality.Modality

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportAll}

/** Represents a finite event emitted by a modality.
  */
@JSExportAll
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
  val modality: Modality

  @JSExport("name")
  def jsName: String = name

  @JSExport("modality")
  def jsModality: String = Modality.toString

  override def toString: String = name
}

/** Description of an avent composed of atomic event, happening over a period of time
  */
trait CompositeEvent extends Event {
  val maybeName: Option[String]
  val expression: CompositeExpression

  @JSExport
  def name: js.UndefOr[String] =
    if (maybeName.isDefined) maybeName.get else js.undefined

  override def toString: String = maybeName.getOrElse(expression.toString)
}
