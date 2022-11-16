package meijs.api.event_system

import meijs.eventbase.Registry
import meijs.eventbase.structures.{AtomicEvent, CompositeEvent, CompositeExpression}

import scala.language.implicitConversions

object Test {

  val testCompositeExpression: CompositeEvent = new CompositeEvent {
    override val maybeName: Option[String] = None
    override val expression: CompositeExpression =
      fromStr("click") + fromStr("click")
  }

  // TODO ! define a scala macro for something like the f string (eg: 'click + 'click)
  // TODO ! Modality precision : keyboard.keyup
  def fromStr(str: String): AtomicEvent = new AtomicEvent {
    override val name: String = str
  }

  Registry.register(testCompositeExpression)

}
