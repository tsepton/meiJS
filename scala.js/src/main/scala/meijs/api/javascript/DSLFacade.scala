package meijs.api.javascript

import meijs.eventbase.structures.{
  And,
  AtomicEvent,
  CompositeEvent,
  CompositeExpression,
  Event,
  FollowedBy,
  Iteration,
  Or
}
import meijs.modality.Modality

import scala.scalajs.js.annotation.{JSExport, JSExportAll, JSExportTopLevel}

// TODO : read this for multiple module exports : https://www.scala-js.org/doc/project/module.html

// TODO : a javascript wrapper for the dsl
// Work in progress...
@JSExportTopLevel("dsl")
@JSExportAll
object ModalityFacade {

  def voice(n: String): JSAtomicEvent = genericMapper(n, Modality.Voice)

  def mouse(n: String): JSAtomicEvent = genericMapper(n, Modality.Mouse)

  def keyboard(n: String): JSAtomicEvent =
    genericMapper(n, Modality.Keyboard)

  def gaze(n: String): JSAtomicEvent = genericMapper(n, Modality.Gaze)

  def hand(n: String): JSAtomicEvent = genericMapper(n, Modality.Hand)

  private def genericMapper(n: String, m: Modality): AtomicEvent = new AtomicEvent {
    override val name: String       = n
    override val modality: Modality = m
  }
}

import scala.language.implicitConversions

@JSExportTopLevel("AtomicEvent")
@JSExportAll
class JSAtomicEvent(val name: String, val modality: Modality) extends AtomicEvent {
  @JSExport("then")
  def jsFollowedBy(e: Event): JSCompositeExpression = super.followedBy(e)

  @JSExport("or")
  def jsOr(e: Event): JSCompositeExpression = super.or(e)

  @JSExport("and")
  def jsAnd(e: Event): JSCompositeExpression = super.and(e)

  @JSExport("repeat")
  def jsIteration(): JSCompositeExpression = super.iteration

}

object JSAtomicEvent {
  implicit def from(e: AtomicEvent): JSAtomicEvent =
    new JSAtomicEvent(e.name, e.modality)
}

@JSExportTopLevel("CompositeEvent")
class JSCompositeEvent(val name: String, val expression: CompositeExpression)
    extends CompositeEvent

object JSCompositeEvent {
  implicit def from(e: CompositeEvent): JSCompositeEvent =
    new JSCompositeEvent(e.name, e.expression)
}

@JSExportTopLevel("CompositeExpression")
class JSCompositeExpression() extends CompositeExpression {

  private val self = this

  def toCompositeEvent: JSCompositeEvent = new CompositeEvent {
    override val name: String                    = super.toString
    override val expression: CompositeExpression = self
  }

  @JSExport("then")
  def jsFollowedBy(e: Event): JSCompositeExpression =
    new JSCompositeExpression.JSFollowedBy(this.toCompositeEvent, e)

  @JSExport("or")
  def jsOr(e: Event): JSCompositeExpression =
    new JSCompositeExpression.JSOr(this.toCompositeEvent, e)

  @JSExport("and")
  def jsAnd(e: Event): JSCompositeExpression =
    new JSCompositeExpression.JSAnd(this.toCompositeEvent, e)

  @JSExport("repeat")
  def jsIteration: JSCompositeExpression =
    new JSCompositeExpression.JSIteration(this.toCompositeEvent)
}

object JSCompositeExpression {

  class JSAnd(left: Event, right: Event)        extends JSCompositeExpression
  class JSOr(left: Event, right: Event)         extends JSCompositeExpression
  class JSFollowedBy(left: Event, right: Event) extends JSCompositeExpression
  class JSIteration(left: Event)                extends JSCompositeExpression

  implicit def from(ce: CompositeExpression): JSCompositeExpression = ce match {
    case And(left, right)        => new JSAnd(left, right)
    case Or(left, right)         => new JSOr(left, right)
    case FollowedBy(left, right) => new JSFollowedBy(left, right)
    case Iteration(left)         => new JSIteration(left)
  }
}
