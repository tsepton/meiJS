package meijs.eventbase

import meijs.eventbase.structures.{CompositeEvent, Event}

import scala.collection.mutable.ListBuffer

object Registry {

  private val _registry: ListBuffer[Event] = ListBuffer()

  /** Alias for register
    */
  def +=[A <: Event](event: A): Unit = register(event)

  /** Register the declaration of a composite event so that its occurrence will be handled by the framework.
    *
    * @param event : the composite event to register
    */
  def register[A <: Event](event: A): Unit =
    _registry += event

  /** Erase this internal state by erasing all previously registered events
    */
  def clean(): Unit = _registry.clear()

  def list: List[Event] = _registry.toList

  def compositeEvents: List[CompositeEvent] = _registry.toList.collect {
    case e: CompositeEvent => e
  }

}
