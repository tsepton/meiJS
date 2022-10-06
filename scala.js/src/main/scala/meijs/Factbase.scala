package meijs

import meijs.structures.Fact

import scala.util.{Failure, Success, Try}
import scala.scalajs.js

object FactBase {

  // Garbage Collector initialisation
  js.timers.setInterval(200) {
    GarbageCollector.collect()
  }

  /**
   * Internal state, it should not be exposed directly
   */
  private var _facts: List[Fact] = Nil

  /**
   * Insert a *fact* at the given *index*
   *
   * Note: if (i > _facts.length), then the fact is appended
   *
   * @param fact a new element to insert at position i
   * @param i the index the fact should be inserted to
   *
   */
  def insert(fact: Fact, i: Int): Unit = {
    val (front, back) = Try(_facts.splitAt(i)) match {
      case Failure(_) => (_facts, Nil)
      case Success(value) => value
    }
    _facts = (front :+ fact) ++ back
  }

  /**
   * Append to the internal factbase an element
   * @param fact the element to be appended
   */
  def append(fact: Fact): Unit = _facts = _facts :+ fact

  /**
   * Alias for append
   */
  def +=(fact: Fact): Unit = append(fact)

  /**
   * Append to the internal factbase a list of elements
   * @param facts the list of elements to be appended
   */
  def append(facts: List[Fact]): Unit = _facts = _facts :++ facts

  /**
   * Alias for append
   */
  def ++=(facts: List[Fact]): Unit = append(facts)

  def flatMap(f: Fact => List[Fact]): List[Fact] = _facts.flatMap(f)

  def map[A](f: Fact => A): List[A]  = _facts.map(f)

  def filter(f: Fact => Boolean): List[Fact] = _facts.filter(f)

  def collect[A <: Fact](pf: PartialFunction[Fact, A]): List[A] = _facts.collect(pf)

  override def toString: String = _facts.toString

  /**
   * The garbage collector should be the only way to remove data from the internal _facts list
   */
  private object GarbageCollector {

    def collect(): Unit = _facts = FactBase._facts.filter(_.isValid)

  }
}
