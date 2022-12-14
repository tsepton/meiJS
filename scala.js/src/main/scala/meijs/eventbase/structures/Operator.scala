package meijs.eventbase.structures

import scala.language.implicitConversions

trait CompositeExpression

case object CompositeExpression {
  implicit def toCompositeEvent(e: CompositeExpression): CompositeEvent = {
    val self = this
    new CompositeEvent {
      override val name: String                    = self.toString
      override val expression: CompositeExpression = e
    }
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

  override def toString: String = f"(${left.toString} $identifier)"
}

sealed trait BinaryOperator extends Operator {
  val left: Event
  val right: Event

  override def toString: String = f"(${left.toString} $identifier ${right.toString})"
}
