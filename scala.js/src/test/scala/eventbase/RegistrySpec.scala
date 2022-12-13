package eventbase

import meijs.eventbase.Registry
import meijs.eventbase.structures.CompositeEvent
import org.scalatest.funsuite.AnyFunSuite

/** Test suite for the Registry object.
  */
class RegistrySpec extends AnyFunSuite {

  import eventbase.MockupData._

  test("Registering simple command") {
    Registry += putThatThere
    assert(Registry.list.length == 1)
  }

  test("Registering and getting back simple command") {
    Registry += putThatThere
    assert(
      Registry.list
        .collect { case c: CompositeEvent => c }
        .find(_.expression == putThatThere.expression)
        .get == putThatThere
    )
  }

}
