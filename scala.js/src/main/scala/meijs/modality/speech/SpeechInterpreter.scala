package meijs.modality.speech

import meijs.modality.ModalityInterpreter
import meijs.modality.speech.web_speech_api._
import org.scalajs.dom.{Event, console}

import scala.util.Try

object SpeechInterpreter extends ModalityInterpreter {

  override def init(): Unit = {

    val recognition =
      Try(new SpeechRecognition()).getOrElse(new WebkitSpeechRecognition())
    val speechRecognitionList =
      Try(new SpeechGrammarList()).getOrElse(new WebkitSpeechGrammarList())

    val colors = List(
      "put",
      "that",
      "there"
    ) // TODO read the MDN documentation
    val grammar =
      f"#JSGF V1.0; grammar colors; public <color> = ${colors.mkString(" | ")};"

    speechRecognitionList.addFromString(grammar, 1)

    recognition.grammars = speechRecognitionList
    recognition.continuous = true
    recognition.lang = "en-US"
    recognition.interimResults = true
    recognition.maxAlternatives = 2

    recognition.addEventListener("result", { (e: Event) => console.log(e) }, false)

    recognition.start()

  }

  // TODO send result to Database
}
