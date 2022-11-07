package meijs.eventbase

import meijs.eventbase.structures.CompositeEvent

import scala.collection.mutable.ListBuffer

object Registry {

  private val _registry: ListBuffer[CompositeEvent] = ListBuffer()

  def registry: List[CompositeEvent] = _registry.toList

  /** Alias for register
    */
  def +=(compositeEvent: CompositeEvent): Unit = register(compositeEvent)

  /** Register the declaration of a composite event so that its occurrence will be handled by the framework.
    *
    * @param compositeEvent : the composite event to register
    */
  def register(compositeEvent: CompositeEvent): Unit =
    _registry += compositeEvent

}
