package eventbase

import meijs.eventbase.Registry
import meijs.{Config, MeiJS}
import org.scalajs.dom.{Event, document}
import org.scalatest.funsuite.AnyFunSuite

class MeiJSIntegrationSpec extends AnyFunSuite {

  val appConfig = new Config(true)

  def fakePutThatThereEventOccurrence(): Unit = {
    MockupData.fakeVoice("put")
    MockupData.fakeVoice("that")
    MockupData.fakeClick()
    MockupData.fakeVoice("there")
    MockupData.fakeClick()
  }

  // TODO for the app integration testing, we'll need a fake dom
  test("Application workflow seems correct as a whole") {

    MeiJS.enable(appConfig)
    MockupData.defaultEvents.foreach(Registry.register)
    assertResult(MockupData.defaultEvents.length)(Registry.list.length)

    var i = 0
    MockupData.defaultEvents foreach { cEvent =>
      document.addEventListener(
        cEvent.maybeName.get,
        { (e: Event) =>
          {
            i += 1
            assert(MockupData.defaultEvents.map(_.maybeName.get) contains e.`type`)
          }
        }
      )
    }

    val n = 10;
    (0 until n).foreach(_ => fakePutThatThereEventOccurrence())
    assertResult(n)(i)
  }

}
