package meijs.api.event_system

private case class EmittedCommand(command: MultimodalEvent, at: Long)

private case object EmittedCommand {

  def from(command: MultimodalEvent): EmittedCommand =
    EmittedCommand(command, at = System.currentTimeMillis() / 1000)

}
