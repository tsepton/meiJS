package meijs.eventbase.recognisers

trait Recogniser {
  def init(): Unit
}

object Default extends Recogniser {

  def init(): Unit = ???

}
