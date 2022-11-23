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
  test("Javascript api") {

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

}
