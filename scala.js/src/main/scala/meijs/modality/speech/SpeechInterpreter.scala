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
    recognition.maxAlternatives = 1
  }

  private def setupEventsHandling(): Unit = {
    recognition.addEventListener(
      "result",
      (
          (e: SpeechRecognitionEvent) => {
            // we asked for interim results => final has already treated data
            val unprocessed = e.results.filterNot(_.isFinal)
            // FIXME unprocessed still has already processed words !
            //console.log(SpeechEvent.from(unprocessed).map(_.name.trim).toJSArray)
            Database ++= (Data from SpeechEvent.from(unprocessed))
          }
      )
    )
  }

  private def setupRecogniserLifecycle(): Unit = {
    // TODO: relaunch the recognition if it stops unexpectedly
    recognition.start()
  }
}
