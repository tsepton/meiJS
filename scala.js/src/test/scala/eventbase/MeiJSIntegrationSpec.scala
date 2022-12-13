package eventbase

import meijs.api.javascript.JSEventSystem
import meijs.eventbase.Registry
import meijs.eventbase.recognisers.state_machine.SMRecogniser
import meijs.{Config, MeiJS}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import scala.scalajs.js

class MeiJSIntegrationSpec extends AnyFunSuite with BeforeAndAfter {

  val appConfigWithEventEmission    = new Config(true)
  val appConfigWithoutEventEmission = new Config(false)

  var counter: Int = 0

  private def fakePutThatThereEventOccurrence(): Unit = {
    MockupData.fakeVoice("put")
    MockupData.fakeVoice("that")
    MockupData.fakeClick()
    MockupData.fakeVoice("there")
    MockupData.fakeClick()
  }

  private def fakeSayHiEventOccurrence(): Unit = {
    MockupData.fakeVoice("say")
    MockupData.fakeVoice("hi")
    MockupData.fakeClick()
  }

  MockupData.defaultEvents foreach { cEvent =>
    JSEventSystem.subscribe(cEvent, _ => counter += 1)
  }

  after {
    Registry.clean()
    SMRecogniser.clean()
  }

  before {
    MeiJS.enable(appConfigWithEventEmission)
  }

  // TODO for the app integration testing, we'll need a fake dom
  test("Ensure the Javascript api event system global workflow") {
    assertResult(MockupData.defaultEvents.length)(Registry.list.length)

    fakePutThatThereEventOccurrence()
    assertResult(1)(counter)
    fakeSayHiEventOccurrence()
    assertResult(2)(counter)
    fakeSayHiEventOccurrence()
    fakePutThatThereEventOccurrence()
    fakeSayHiEventOccurrence()
    fakeSayHiEventOccurrence()
    fakeSayHiEventOccurrence()
    assertResult(7)(counter)
  }

  test("Ensure timeout for unstable nodes of state machines works") {
    assertResult(MockupData.defaultEvents.length)(Registry.list.length)

    MockupData.fakeVoice("put")
    MockupData.fakeVoice("that")
    MockupData.fakeClick()
    js.timers.setTimeout(
      1000
    ) {} // TODO once the value config setup has been done use that (value + 1)
    MockupData.fakeVoice("there")
    MockupData.fakeClick()
    assertResult(0)(counter)
  }

}
