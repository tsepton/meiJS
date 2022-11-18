package meijs

import meijs.api.event_system.EventSystem
import meijs.eventbase.recognisers.state_machine.SMRecogniser
import meijs.eventbase.structures.{CompositeEvent, CompositeExpression}
import meijs.eventbase.{Database, Registry}
import meijs.modality.Modality
import org.scalajs.dom

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object MeiJS {

  val putThatThere: CompositeEvent = new CompositeEvent {
    override val maybeName: Option[String] = Some("put-that-there")
    override val expression: CompositeExpression =
      Modality.Voice("put") `;`
        (Modality.Mouse("click") + Modality.Voice("that")) `;`
        (Modality.Mouse("click") + Modality.Voice("there"))
  }

  val updateThatColor: CompositeEvent = new CompositeEvent {
    override val maybeName: Option[String] = Some("update-that-color")
    override val expression: CompositeExpression = Modality.Voice("update") `;`
      (Modality.Voice("that") + Modality.Mouse("click")) `;`
      Modality.Voice("colour")
  }

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

    Registry.register(putThatThere)
    Registry.register(updateThatColor)

    // Modalities activation
    modality.dom.DomInterpreter.init()
    modality.speech.SpeechInterpreter.init()
    SMRecogniser.init()

    // APIs activation
    if (config.useEventSystem) enableEventSystem(100)
  }

  private def enableEventSystem(intervalInMs: Int): Unit = {
    EventSystem.init(intervalInMs)
    dom.console.info("Event System Interface successfully enabled")
  }

}

class Config(val useEventSystem: Boolean) extends js.Object
