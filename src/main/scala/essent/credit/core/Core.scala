package essent.credit.core

import akka.actor.{ActorContext, ActorRef}
import akka.routing.FromConfig
import essent.credit.ClusterDefinitions._

object Core {

  trait CreditProvider {
    def newCreditRouter(context: ActorContext): ActorRef
  }

  trait DefaultCreditProvider extends CreditProvider {
    def newCreditRouter(context: ActorContext): ActorRef = context.actorOf(FromConfig.props(), name = CreditRouterName)
  }
}
