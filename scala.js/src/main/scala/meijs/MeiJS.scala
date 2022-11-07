package meijs

import meijs.api.event_system.MultimodalEventSystem
import meijs.eventbase.Database
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
    modality.dom.Dom.init()

    // APIs activation
    if (config.useEventSystem) enableEventSystem(50)

    // Faking fusion event creation
    /*    js.timers.setInterval(1000) {
      EventBase += CompletedCommand()
    }*/
  }

  private def enableEventSystem(intervalInMs: Int = 50): Unit = {
    MultimodalEventSystem.init(intervalInMs)
    dom.console.info("Event System Interface successfully enabled")
  }

}

// FIXME: Find a proper solution to configure the framework
class Config(val useEventSystem: Boolean) extends js.Object
