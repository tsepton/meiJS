package meijs.modality.speech

import meijs.eventbase.Database
import meijs.eventbase.structures.Data
import meijs.modality.ModalityInterpreter
import meijs.modality.speech.web_speech_api._

import scala.collection.mutable.ListBuffer
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
    // TODO
    // val colors: List[String] = Registry.list.map(_.name)
    // val grammar = f"#JSGF V1.0; grammar colors; public <color> = ${colors.mkString(" | ")};"
    // speechRecognitionList.addFromString(grammar, 1)

    recognition.grammars = speechRecognitionList
    recognition.continuous = true
    recognition.lang = "en-US"
    recognition.interimResults = true
    recognition.maxAlternatives = 5
  }

  private def setupEventsHandling(): Unit = {
    val buffer: ListBuffer[String] = ListBuffer()
    recognition.addEventListener(
      "result",
      { (e: SpeechRecognitionEvent) =>
        val unprocessed = e.results.last
        if (unprocessed.isFinal) buffer.clear()
        else {
          buffer appendAll SpeechEvent.from(unprocessed).map(_.name)
          Database ++= (Data from SpeechEvent.from(unprocessed))
        }
      }
    )
  }

  private def setupRecogniserLifecycle(): Unit = {
    // TODO: relaunch the recognition if it stops unexpectedly
    recognition.start()
  }
}
