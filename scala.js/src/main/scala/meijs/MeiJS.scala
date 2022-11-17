package meijs

import meijs.api.event_system.EventSystem
import meijs.eventbase.Database
import meijs.eventbase.recognisers.state_machine.SMRecogniser
import org.scalajs.dom

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

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
