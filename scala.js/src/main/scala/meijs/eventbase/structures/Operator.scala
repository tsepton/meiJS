package meijs.eventbase.structures

import scala.language.implicitConversions

sealed trait CompositeExpression {
  override def toString: String =
    "todo" // TODO - Create a human readable simplified composite expression
}

case object CompositeExpression {
  implicit def toCompositeEvent(e: CompositeExpression): CompositeEvent =
    new CompositeEvent {
      override val maybeName: Option[String]       = Option.empty
      override val expression: CompositeExpression = e
    }
}

final case class And(left: Event, right: Event)
    extends CompositeExpression
    with BinaryOperator {
  val identifier = "+"
}

final case class Or(left: Event, right: Event)
    extends CompositeExpression
    with BinaryOperator {
  val identifier = "|"
}

final case class FollowedBy(left: Event, right: Event)
    extends CompositeExpression
    with BinaryOperator {
  val identifier = ";"
}

final case class Iteration(left: Event)
    extends CompositeExpression
    with UnaryOperator {
  val identifier = "*"
}

sealed trait Operator {
  val identifier: String
}

sealed trait UnaryOperator extends Operator {
  val left: Event

  override def toString: String = left.toString + identifier
}

sealed trait BinaryOperator extends Operator {
  val left: Event
  val right: Event

  override def toString: String = left.toString + identifier + right
}
