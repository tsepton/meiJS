package meijs.eventbase

import meijs.eventbase.structures.{CompositeEvent, Event}

import scala.collection.mutable.ListBuffer

object Registry {

  private val registry: ListBuffer[Event] = ListBuffer()

  /** Alias for register(e: Event) */
  def +=[A <: Event](event: A): Unit = register(event)

  /** Register the declaration of a composite event so that its occurrence will be handled by the framework.
    *
    * @param event : the composite event to register
    */
  def register[A <: Event](event: A): Unit = registry += event

  /** Alias for register(e: Iterable[Event]) */
  def ++=[A <: Event](events: Iterable[A]): Unit = register(events)

  /** Register the declaration of a composite event so that its occurrence will be handled by the framework.
    *
    * @param events : an iterable of all the composite events to register
    */
  def register[A <: Event](events: Iterable[A]): Unit = registry ++= events

  /** Erase this internal state by erasing all previously registered events */
  def clean(): Unit = registry.clear()

  /** List all registered events present inside _registry */
  def list: List[Event] = registry.toList

  /** List all the registered composite events */
  def compositeEvents: List[CompositeEvent] = registry.toList.collect {
    case e: CompositeEvent => e
  }

  /** List all the registered atomic events */
  def atomicEvents: List[CompositeEvent] = registry.toList.collect {
    case e: CompositeEvent => e
  }

}
