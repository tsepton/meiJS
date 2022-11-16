package meijs.modality.speech.web_speech_api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/** The SpeechRecognitionResultList interface of the Web Speech API represents a list of SpeechRecognitionResult objects, or a single one if results are being captured in continuous mode.
  */
@js.native
@JSGlobal
class SpeechRecognitionResultList extends js.Object {

  /** Returns the length of the "array" — the number of SpeechRecognitionResult objects in the list. */
  val length: Int = js.native

  /** A standard getter that allows SpeechRecognitionResult objects in the list to be accessed via array syntax
    *
    * @param index of the item to retrieve
    */
  def item(index: Int): SpeechRecognitionResult = js.native

}
