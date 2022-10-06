package meijs

import meijs.structures.Event

object Main {
  def main(args: Array[String]): Unit = {

    scala.scalajs.js.timers.setInterval(500) {
      FactBase.insert(Event((System.currentTimeMillis / 1000) + 2000))
      println(FactBase)
    }

  }

}
