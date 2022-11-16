package meijs.modality.speech.web_speech_api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/** The SpeechGrammarList interface of the Web Speech API represents a list of SpeechGrammar objects containing words or patterns of words that we want the recognition service to recognize.
  *
  * Grammar is defined using JSpeech Grammar Format (JSGF.) Other formats may also be supported in the future.
  */
@js.native
@JSGlobal
class SpeechGrammarList {

  /** Returns the number of SpeechGrammar objects contained in the SpeechGrammarList. */
  val length: Int = js.native

  /** The addFromString() method of the SpeechGrammarList interface takes a grammar present in a specific string within the code base (e.g. stored in a variable) and adds it to the SpeechGrammarList as a new SpeechGrammar object.
    *
    * @param string A string representing the grammar to be added.
    */
  def addFromString(string: String): Unit = js.native

  /** The addFromString() method of the SpeechGrammarList interface takes a grammar present in a specific string within the code base (e.g. stored in a variable) and adds it to the SpeechGrammarList as a new SpeechGrammar object.
    *
    * @param string A string representing the grammar to be added.
    * @param weight A float representing the weight of the grammar relative to other grammars present in the SpeechGrammarList. The weight means the importance of this grammar, or the likelihood that it will be recognized by the speech recognition service. The value can be between 0.0 and 1.0; If not specified, the default used is 1.0.
    * @return
    */
  def addFromString(string: String, weight: Float): Unit = js.native

  /** The addFromURI() method of the SpeechGrammarList interface takes a grammar present at a specific URI and adds it to the SpeechGrammarList as a new SpeechGrammar object.
    *
    * @param src A string representing the URI of the grammar to be added.
    */
  def addFromURI(src: String): Unit = js.native

  /** The addFromURI() method of the SpeechGrammarList interface takes a grammar present at a specific URI and adds it to the SpeechGrammarList as a new SpeechGrammar object.
    *
    * @param src    A string representing the URI of the grammar to be added.
    * @param weight : A float representing the weight of the grammar relative to other grammars present in the SpeechGrammarList. The weight means the importance of this grammar, or the likelihood that it will be recognized by the speech recognition service. The value can be between 0.0 and 1.0; If not specified, the default used is 1.0.
    */
  def addFromURI(src: String, weight: Float): Unit = js.native

  /** Standard getter — allows individual SpeechGrammar objects to be retrieved from the SpeechGrammarList using array syntax.
    *
    * @param index of the item to retrieve
    */
  def item(index: Int): SpeechGrammar = js.native

}
