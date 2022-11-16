package meijs.api.facade

import meijs.eventbase.Database
import meijs.eventbase.structures.Data

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

@JSExportTopLevel("database")
@JSExportAll
object DatabaseFacade {

  import js.JSConverters._

  def length: Int = Database.length

  def events: js.Array[Data] = Database.events.toJSArray

  def append(event: Data): Unit = Database.append(event)
}
