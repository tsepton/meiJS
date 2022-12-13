package meijs.modality.speech.web_speech_api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
/** The SpeechRecognitionAlternative interface of the Web Speech API represents a single word that has been recognized by the speech recognition service.
  */
class SpeechRecognitionAlternative() extends js.Object {

  /** Returns a numeric estimate between 0 and 1 of how confident the speech recognition system is that the recognition is correct.
    */
  val confidence: Int = js.native

  /** Returns a string containing the transcript of the recognized word.
    */
  val transcript: String = js.native
}
