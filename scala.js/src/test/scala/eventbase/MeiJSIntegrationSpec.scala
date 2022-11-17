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

  test("Application workflow seems correct as a whole") {

    MeiJS.enable(appConfig)
    MockupData.defaultEvents foreach Registry.register

    fakePutThatThereEventOccurrence()

    MockupData.defaultEvents foreach { cEvent =>
      document.addEventListener(
        cEvent.maybeName.get,
        { (e: Event) =>
          MockupData.defaultEvents.map(_.maybeName.get) contains e.`type`
        }
      )
    }
  }

}
