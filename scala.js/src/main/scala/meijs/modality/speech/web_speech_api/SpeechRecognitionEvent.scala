package meijs.modality.speech.web_speech_api

import org.scalajs.dom.Event

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

/** The SpeechRecognitionEvent interface of the Web Speech API represents the event object for the result and nomatch events, and contains all the data associated with an interim or final speech recognition result.
  */
@js.native
@JSGlobal
class SpeechRecognitionEvent(typeArg: String) extends Event(typeArg) {

  /** Returns a SpeechRecognitionResultList object representing all the speech recognition results for the current session. */
  val results: SpeechRecognitionResultList = js.native

  /** Returns the lowest index value result in the SpeechRecognitionResultList "array" that has actually changed. */
  val resultIndex: Int = js.native

  /** Returns the semantic meaning of what the user said. */
  @Deprecated
  val interpretation: js.Any = js.native

  /** Returns an Extensible MultiModal Annotation markup language (EMMA) — XML — representation of the result. */
  @Deprecated
  val emma: String = js.native

}

@js.native
@JSGlobal("webkitSpeechRecognitionEvent")
class WebkitSpeechRecognitionEvent(typeArg: String)
    extends SpeechRecognitionEvent(typeArg)
