package meijs.modality.speech.web_speech_api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/** The SpeechRecognitionResult interface of the Web Speech API represents a single recognition match, which may contain multiple SpeechRecognitionAlternative objects.
  */
@js.native
@JSGlobal
class SpeechRecognitionResult extends js.Object {

  /** A boolean value that states whether this result is final (true) or not (false) — if so, then this is the final time this result will be returned; if not, then this result is an interim result, and may be updated later on. */
  val isFinal: Boolean = js.native

  /** Returns the length of the "array" — the number of SpeechRecognitionAlternative objects contained in the result (also referred to as "n-best alternatives".) */
  val length: Int = js.native

  /** A standard getter that allows SpeechRecognitionAlternative objects within the result to be accessed via array syntax.
    *
    * @param index of the item to retrieve
    */
  def item(index: Int): SpeechRecognitionAlternative = js.native
}
