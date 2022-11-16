package meijs.modality.speech.web_speech_api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
/** The SpeechGrammar interface of the Web Speech API represents a set of words or patterns of words that we want the recognition service to recognize.
  *
  * Grammar is defined using JSpeech Grammar Format (JSGF.) Other formats may also be supported in the future.
  */
class SpeechGrammar() extends js.Object {

  /** Sets and returns a string containing the grammar from within in the SpeechGrammar object instance.
    */
  var weight: Int = js.native

  /** Returns a numeric estimate between 0 and 1 of how confident the speech recognition system is that the recognition is correct.
    */
  var src: String = js.native

}
