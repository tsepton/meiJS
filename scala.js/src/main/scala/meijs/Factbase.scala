package meijs

import meijs.structures.{Fact, Event}

object Factbase {

  scala.scalajs.js.timers.setInterval(200) {
    GarbageCollector.collect()
  }

  // For now, using a simple list:
  // https://docs.scala-lang.org/overviews/collections-2.13/performance-characteristics.html
  private var facts: List[Fact] = Nil

  def insert(fact: Fact): Unit = facts = facts :+ fact

  override def toString: String = facts.toString()

  private object GarbageCollector {

    def collect(): Unit = facts = Factbase.facts.filter(_.isValid)

  }
}