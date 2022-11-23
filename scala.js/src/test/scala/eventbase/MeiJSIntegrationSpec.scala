package eventbase

import meijs.eventbase.{Database, Registry}
import meijs.{Config, MeiJS}
import org.scalajs.dom.{Event, document}
import org.scalatest.funsuite.AnyFunSuite

class MeiJSIntegrationSpec extends AnyFunSuite {

  val appConfigWithEventEmission    = new Config(true)
  val appConfigWithoutEventEmission = new Config(false)

  def fakePutThatThereEventOccurrence(): Unit = {
    MockupData.fakeVoice("put")
    MockupData.fakeVoice("that")
    MockupData.fakeClick()
    MockupData.fakeVoice("there")
    MockupData.fakeClick()
  }

  // TODO for the app integration testing, we'll need a fake dom
  test("Event emission") {

    MeiJS.enable(appConfigWithEventEmission)
    MockupData.defaultEvents.foreach(x => Registry.register(x))
    assertResult(MockupData.defaultEvents.length)(Registry.list.length)

    var i = 0
    MockupData.defaultEvents foreach { cEvent =>
      document.addEventListener(
        cEvent.name,
        { (e: Event) =>
          {
            i += 1
            assert(MockupData.defaultEvents.map(_.name) contains e.`type`)
          }
        }
      )
    }

    val n = 10;
    (0 until n).foreach(_ => fakePutThatThereEventOccurrence())
    assertResult(n)(i)
  }

  // TODO for the app integration testing, we'll need a fake dom
  test("State machine putting result in db") {

    MeiJS.enable(appConfigWithoutEventEmission)
    Registry += MockupData.putThatThere

    assert(Registry.list.contains(MockupData.putThatThere))

    MockupData.fakeVoice("put")
    MockupData.fakeClick()
    MockupData.fakeVoice("that")
    MockupData.fakeClick()
    MockupData.fakeVoice("there")

    val dataList = Database.filter(_.event == MockupData.putThatThere)
    assert(dataList.length == 1)
    dataList.flatMap(data => data.occurrence).zipWithIndex.foreach {
      case (e, 0) => e.name == "put"
      case (e, 1) => e.name == "click"
      case (e, 2) => e.name == "that"
      case (e, 3) => e.name == "click"
      case (e, 4) => e.name == "there"
    }
  }

}
