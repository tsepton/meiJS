package meijs.modality.speech

import meijs.eventbase.structures.AtomicEvent
import meijs.modality.Modality
import meijs.modality.speech.web_speech_api.SpeechRecognitionResultList.toList
import meijs.modality.speech.web_speech_api.{
  SpeechRecognitionResult,
  SpeechRecognitionResultList
}

import scala.language.implicitConversions

sealed trait SpeechEvent extends AtomicEvent {
  override val name: String
  override val modality: Modality = Modality.Voice
}

object SpeechEvent {

  def from(result: SpeechRecognitionResult): List[SpeechEvent] =
    result.flatMap(alt =>
      alt.transcript.split(" ").toList match {
        case words if words.length == 1 => List(Word(words.head))
        case words =>
          List(
            List(Sentence(alt.transcript.trim)),
            words.map(str => Word(str))
          ).flatten
      }
    )

  def from(results: List[SpeechRecognitionResult]): List[SpeechEvent] =
    results.flatMap(result => from(result))

  def from(results: SpeechRecognitionResultList): List[SpeechEvent] =
    from(toList(results))

}

case class Word(name: String) extends SpeechEvent

case class Sentence(name: String) extends SpeechEvent
