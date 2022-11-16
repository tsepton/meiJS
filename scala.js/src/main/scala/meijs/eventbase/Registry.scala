package meijs.eventbase

import meijs.eventbase.structures.{CompositeEvent, CompositeExpression}

import scala.collection.mutable.ListBuffer

object Registry {

  private val _registry: ListBuffer[CompositeEvent] = ListBuffer()

  /** Alias for register
    */
  def +=(compositeEvent: CompositeEvent): Unit = register(compositeEvent)

  /** Register the declaration of a composite event so that its occurrence will be handled by the framework.
    *
    * @param compositeEvent : the composite event to register
    */
  def register(compositeEvent: CompositeEvent): Unit =
    _registry += compositeEvent

  /** Erase this internal state by erasing all previously registered events
    */
  def clean(): Unit = _registry.clear()

  def get(expression: CompositeExpression): Option[CompositeEvent] =
    list.find(_.expression == expression)

  def list: List[CompositeEvent] = _registry.toList

}
