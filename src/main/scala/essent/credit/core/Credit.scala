package essent.credit.core

import akka.actor.{Actor, ActorContext, ActorLogging, ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import essent.credit.Amount
import essent.credit.ClusterDefinitions._

class Credit extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  val amount: Amount = 0

  def receive = {
    case _ => log.info("CREDIT: {}", amount)
  }
}


object Credit {

  trait CreditProvider {
    def newCredit(context: ActorContext): ActorRef
  }

  trait DefaultCreditProvider {
    def newCredit(context: ActorContext): ActorRef = context.actorOf(FromConfig.props(), name = CreditRouterName)
  }


  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "0" else args(0)

    val config = ConfigFactory
      .parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$CoreClusterRole]"))
      .withFallback(ConfigFactory.load("application"))


    val system = ActorSystem(CreditCluster, config)
    system.actorOf(Props[Credit] , name = "Credit")
  }
}