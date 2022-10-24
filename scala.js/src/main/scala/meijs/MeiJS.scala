package meijs

import meijs.api.event_system.MultimodalEventSystem
import meijs.factbase.FactBase
import meijs.factbase.structures.CompletedCommand

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object MeiJS {

  // TODO : Set up a config file
  def main(args: Array[String]): Unit = ???

  @JSExport
  def enable(): Unit = {
    FactBase.init(10000)
    MultimodalEventSystem.init()

    // Faking fusion event creation
    // TODO: Implement fusion
    js.timers.setInterval(1000) {
      FactBase += CompletedCommand()
    }
  }
}

sealed trait Config
