package meijs

import meijs.api.event_system.MultimodalEventSystem
import meijs.structures.CompletedCommand

import scala.scalajs.js

object Main {
  def main(args: Array[String]): Unit = {
    FactBase.init()
    MultimodalEventSystem.init()

    // Faking fusion event creation
    js.timers.setInterval(1000) {
      FactBase += CompletedCommand()
    }
  }

}
