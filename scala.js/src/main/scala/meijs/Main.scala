package meijs

import meijs.api.event_system.MultimodalEventSystem
import meijs.factbase.FactBase
import meijs.factbase.structures.CompletedCommand

import scala.scalajs.js

object Main {

  // TODO : Set up a config file
  def main(args: Array[String]): Unit = {
    FactBase.init(10000)
    MultimodalEventSystem.init()

    // Faking fusion event creation
    js.timers.setInterval(1000) {
      FactBase += CompletedCommand()
    }
  }

}
