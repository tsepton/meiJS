package meijs.eventbase.recognisers.state_machine

import meijs.eventbase.Registry
import meijs.eventbase.recognisers.Recogniser
import meijs.eventbase.structures._

import scala.collection.mutable.ListBuffer

object SMRecogniser extends Recogniser {

  var states: ListBuffer[State] = ListBuffer.empty
  var currentStateIndex: Int = 0

  def init(): Unit = Registry.registry.foreach { c =>
    val put: CompositeEvent = ???
    val that: CompositeEvent = ???
    val click1: CompositeEvent = ???
    val there: CompositeEvent = ???
    val click2: CompositeEvent = ???

    put `;` (that + click1) `;` (there | click2)
    put.followedBy(that.and(click1)).followedBy(there.or(click2))
    FollowedBy(FollowedBy(put, And(that, click1)), Or(there, click2))

    FollowedBy(
      FollowedBy(
        put,
        And(that, click1)
      ),
      Or(there, click2)
    )

    createStateMachine(c, StateMachine())
  }

  // FIXME: actually not tail recursive
  // @tailrec
  def createStateMachine(event: Event, smAcc: StateMachine): StateMachine = {
    event match {
      case e: AtomicEvent => StateMachine.initWithSimpleEvent(e)
      case e: CompositeEvent =>
        e.expression match {
          case exp: Iteration => createStateMachine(exp.left, smAcc).loop
          case exp: And =>
            createStateMachine(exp.left, smAcc) permute
              createStateMachine(exp.right, smAcc)
          case exp: Or =>
            createStateMachine(exp.left, smAcc) overlay
              createStateMachine(exp.right, smAcc)
          case exp: FollowedBy =>
            createStateMachine(exp.left, smAcc) concatenate
              createStateMachine(exp.right, smAcc)
        }
      case _ => ??? // TODO
    }
  }

}
