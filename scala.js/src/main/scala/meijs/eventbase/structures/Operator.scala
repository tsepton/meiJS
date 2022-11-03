package meijs.eventbase.structures

import scala.scalajs.js.annotation.JSExportTopLevel

sealed trait Operator

@JSExportTopLevel("And")
final case class And(a: Event, b: Event) extends CompositeEvent with Operator

@JSExportTopLevel("Or")
final case class Or(a: Event, b: Event) extends CompositeEvent with Operator

@JSExportTopLevel("Iteration")
final case class Iteration(a: Event) extends CompositeEvent with Operator

@JSExportTopLevel("FollowedBy")
final case class FollowedBy(a: Event, b: Event)
    extends CompositeEvent
    with Operator
