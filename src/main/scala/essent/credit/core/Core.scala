package essent.credit.core

import akka.actor.{ActorContext, ActorRef}
import akka.routing.FromConfig
import essent.credit.ClusterDefinitions._

object Core {

  trait CreditProvider {
    def newCredit(context: ActorContext): ActorRef
  }

  trait DefaultCreditProvider {
    def newCredit(context: ActorContext): ActorRef = context.actorOf(FromConfig.props(), name = CreditRouterName)
  }
}
