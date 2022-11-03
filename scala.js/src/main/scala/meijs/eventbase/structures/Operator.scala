package meijs.eventbase.structures

import scala.scalajs.js.annotation.JSExportTopLevel

sealed trait Operator

@JSExportTopLevel("And")
final case class And(name: String)(a: Event, b: Event)
    extends CompositeEvent
    with Operator

@JSExportTopLevel("Or")
final case class Or(name: String)(a: Event, b: Event)
    extends CompositeEvent
    with Operator

@JSExportTopLevel("Iteration")
final case class Iteration(name: String)(a: Event)
    extends CompositeEvent
    with Operator

@JSExportTopLevel("FollowedBy")
final case class FollowedBy(name: String)(a: Event, b: Event)
    extends CompositeEvent
    with Operator
