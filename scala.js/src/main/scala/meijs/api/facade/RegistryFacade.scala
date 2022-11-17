package meijs.api.facade

import meijs.eventbase.Registry
import meijs.eventbase.structures.CompositeEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

@JSExportTopLevel("registry")
@JSExportAll
object RegistryFacade {

  import js.JSConverters._

  def list: js.Array[CompositeEvent] = Registry.list.toJSArray

}