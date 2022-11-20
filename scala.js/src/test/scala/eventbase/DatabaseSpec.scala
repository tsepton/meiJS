package eventbase

import meijs.eventbase.Database
import meijs.eventbase.structures.Data
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}

class DatabaseSpec extends AnyFunSuite with BeforeAndAfter with BeforeAndAfterAll {

  import MockupData._

  override def beforeAll(): Unit = {
    Database.init(50)
  }

  after {
    Database.clear()
  }

  test("Add event into database") {
    assert(Database.length == 0)
    Database += Data.from(putThatThere, Nil)
    assert(Database.length == 1)
  }

}
