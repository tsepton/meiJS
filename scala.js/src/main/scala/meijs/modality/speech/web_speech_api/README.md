# Web Speech API facades

Facades based on the [MDN documentation](https://developer.mozilla.org/en-US/docs/Web/API/Web_Speech_API) to be used with ScalaJS.

## Getting started

For a complete example see [Using the Web Speech API page](https://developer.mozilla.org/en-US/docs/Web/API/Web_Speech_API/Using_the_Web_Speech_API) on the MDN documentation. However, here is a quick demonstration of the speech recognition API adapted from the same page:

```scala
import org.scalajs.dom.{Event, console}

import scala.util.Try

object SpeechRecognition {

  val recognition: SpeechRecognition =
    Try(new SpeechRecognition()).getOrElse(new WebkitSpeechRecognition())
  val speechRecognitionList: SpeechGrammarList =
    Try(new SpeechGrammarList()).getOrElse(new WebkitSpeechGrammarList())

  override def main(args: Array[String]): Unit = {

    val colors = List("red", "orange", "blue") // ...
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
}
```
