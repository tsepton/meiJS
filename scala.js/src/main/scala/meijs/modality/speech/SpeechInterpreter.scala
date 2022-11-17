package meijs.modality.speech

import meijs.eventbase.Database
import meijs.eventbase.structures.Data
import meijs.modality.ModalityInterpreter
import meijs.modality.speech.web_speech_api._

import scala.util.Try

object SpeechInterpreter extends ModalityInterpreter {

  val recognition: SpeechRecognition =
    Try(new SpeechRecognition()).getOrElse(new WebkitSpeechRecognition())
  val speechRecognitionList: SpeechGrammarList =
    Try(new SpeechGrammarList()).getOrElse(new WebkitSpeechGrammarList())

  override def init(): Unit = {
    setupRecognitionSettings()
    setupRecogniserLifecycle()
    setupEventsHandling()
  }

  private def setupRecognitionSettings(): Unit = {
    val colors = List("put", "that", "there") // TODO
    val grammar =
      f"#JSGF V1.0; grammar colors; public <color> = ${colors.mkString(" | ")};"

    speechRecognitionList.addFromString(grammar, 1)

    recognition.grammars = speechRecognitionList
    recognition.continuous = true
    recognition.lang = "en-US"
    recognition.interimResults = true
    recognition.maxAlternatives = 2
  }

  private def setupEventsHandling(): Unit = {
    recognition.addEventListener(
      "result",
      (
          (e: SpeechRecognitionEvent) =>
            Database ++= (Data from SpeechEvent.from(e.results))
      )
    )
  }

  private def setupRecogniserLifecycle(): Unit = {
    // TODO: relaunch the recognition if it stops unexpectedly
    recognition.start()
  }
}
