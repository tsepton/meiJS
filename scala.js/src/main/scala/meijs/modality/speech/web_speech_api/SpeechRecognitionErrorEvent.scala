package meijs.modality.speech.web_speech_api

import org.scalajs.dom.Event

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
/** The SpeechRecognitionErrorEvent interface of the Web Speech API represents error messages from the recognition service.
  */
class SpeechRecognitionErrorEvent(typeArg: String) extends Event(typeArg) {

  /** Returns a message describing the error in more detail.
    */
  val message: String = js.native

  /** Returns the type of error raised.
    *
    * See: https://developer.mozilla.org/en-US/docs/Web/API/SpeechRecognitionErrorEvent/error
    */
  val error: String = js.native

}
