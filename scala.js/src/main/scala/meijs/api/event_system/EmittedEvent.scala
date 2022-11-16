package meijs.api.event_system

case class EmittedEvent(event: JavascriptEvent, at: Long)

object EmittedEvent {
  def from(event: JavascriptEvent): EmittedEvent =
    new EmittedEvent(event, System.currentTimeMillis() / 1000)
}
