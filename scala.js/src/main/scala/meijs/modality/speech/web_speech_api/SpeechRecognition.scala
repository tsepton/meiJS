package meijs.modality.speech.web_speech_api

import org.scalajs.dom.EventTarget

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/** The SpeechRecognition interface of the Web Speech API is the controller interface for the recognition service; this also handles the SpeechRecognitionEvent sent from the recognition service. */
@js.native
@JSGlobal
class SpeechRecognition extends EventTarget {

  /** Returns and sets a collection of SpeechGrammar objects that represent the grammars that will be understood by the current SpeechRecognition.
    */
  var grammars: SpeechGrammarList = js.native

  /** Returns and sets the language of the current SpeechRecognition. If not specified, this defaults to the HTML lang attribute value, or the user agent's language setting if that isn't set either.
    */
  var lang: String = js.native

  /** Controls whether continuous results are returned for each recognition, or only a single result. Defaults to single (false.)
    */
  var continuous: Boolean = js.native

  /** Controls whether interim results should be returned (true) or not (false.) Interim results are results that are not yet final (e.g. the SpeechRecognitionResult.isFinal property is false.)
    */
  var interimResults: Boolean = js.native

  /** Sets the maximum number of SpeechRecognitionAlternatives provided per result. The default value is 1.
    */
  var maxAlternatives: Int = js.native

  /** Stops the speech recognition service from listening to incoming audio, and doesn't attempt to return a SpeechRecognitionResult.
    */
  def abort(): Unit = js.native

  /** Starts the speech recognition service listening to incoming audio with intent to recognize grammars associated with the current SpeechRecognition.
    */
  def start(): Unit = js.native

  /** Stops the speech recognition service from listening to incoming audio, and attempts to return a SpeechRecognitionResult using the audio captured so far.
    */
  def stop(): Unit = js.native

  // TODO events : https://developer.mozilla.org/en-US/docs/Web/API/SpeechRecognition#events
}

@js.native
@JSGlobal("webkitSpeechRecognition")
class WebkitSpeechRecognition extends SpeechRecognition
