package meijs

import meijs.api.event_system.EventSystem
import meijs.eventbase.recognisers.state_machine.SMRecogniser
import meijs.eventbase.structures.{CompositeEvent, CompositeExpression, Data}
import meijs.eventbase.{Database, Registry}
import meijs.modality.Modality.{Mouse, Voice}
import org.scalajs.dom

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.timers.SetIntervalHandle

object MeiJS {

  def main(args: Array[String]): Unit = dom.console.info("""
      |
      |    __  __     _    _ ___
      |   |  \/  |___(_)_ | / __|
      |   | |\/| / -_) | || \__ \
      |   |_|  |_\___|_|\__/|___/
      |
      |meijs.tsepton.be.
      |
      |""".stripMargin)

  @JSExportTopLevel("enable")
  def enable(config: Config): Unit = {
    // EventBase
    Database.init(50 * 2)

    Test.test()

    // Modalities activation
    modality.dom.DomInterpreter.init()
    SMRecogniser.init()

    // APIs activation
    if (config.useEventSystem) enableEventSystem(50)
  }

  private def enableEventSystem(intervalInMs: Int = 50): Unit = {
    EventSystem.init(intervalInMs)
    dom.console.info("Event System Interface successfully enabled")
  }

}

// FIXME: Find a proper solution to configure the framework
class Config(val useEventSystem: Boolean) extends js.Object

object Test {

  val putThatThere: CompositeEvent = new CompositeEvent {
    override val maybeName: Option[String] = Some("put-that-there")
    override val expression: CompositeExpression =
      Voice("put") `;`
        (Voice("that") + Mouse("click")) `;`
        (Voice("there") + Mouse("click"))
  }

  // TODO ! define a scala macro for something like the f string (eg: 'click + 'click)

  Registry.register(putThatThere)

  def test(): SetIntervalHandle = scalajs.js.timers.setInterval(100) {
    fakeVoice("put")
    fakeVoice("that")
    fakeClick()
    fakeVoice("there")
    fakeClick()
  }

  def fakeVoice(word: String): Unit = Database += (Data from Voice(word))

  def fakeClick(): Unit = Database += (Data from Mouse("click"))
}
