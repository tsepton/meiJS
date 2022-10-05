package meijs.structures


trait Fact {
  val validationTime: Long
  val emissionTime: Long = System.currentTimeMillis / 1000

  def validUntil: Long = emissionTime + validationTime
  def isValid: Boolean = validUntil < (System.currentTimeMillis / 1000)
}

case class Event(validationTime: Long) extends Fact  {

}
