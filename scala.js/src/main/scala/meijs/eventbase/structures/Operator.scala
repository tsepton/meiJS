package meijs.eventbase.structures

import scala.language.implicitConversions

sealed trait CompositeExpression

case object CompositeExpression {
  implicit def toCompositeEvent(e: CompositeExpression): CompositeEvent =
    new CompositeEvent {
      override val maybeName: Option[String] = Option.empty
      override val expression: CompositeExpression = e
    }
}

final case class And(a: Event, b: Event) extends CompositeExpression

final case class Or(a: Event, b: Event) extends CompositeExpression

final case class FollowedBy(a: Event, b: Event) extends CompositeExpression

final case class Iteration(a: Event) extends CompositeExpression
