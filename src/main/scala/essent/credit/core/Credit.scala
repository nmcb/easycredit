package essent.credit.core

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import essent.credit.Amount
import essent.credit.ClusterDefinitions._

class Credit extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  var amount: Amount = 0.0

  def receive = {
    case msg: Any => log.info("CREDIT {} - {}", amount, msg)
  }
}


object Credit {
  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "2551" else args(0)

    val config = ConfigFactory
      .parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$CoreClusterRole]"))
      .withFallback(ConfigFactory.load("application"))


    val system = ActorSystem(CreditCluster, config)
    Cluster(system) registerOnMemberUp {
      system.actorOf(Props[Credit] , name = CreditActorName)
    }
  }
}