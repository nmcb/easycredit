package essent.credit.core

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import essent.credit.ClusterDefinitions._

class Credit extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  val credit = 0

  def receive = {
    case _ => log.info("CREDIT: {}", credit)
  }
}


object Credit {

  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "0" else args(0)

    val config = ConfigFactory
    .parseString(s"akka.remote.netty.tcp.port=$port")
    .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$CoreRole]"))
    .withFallback(ConfigFactory.load("application"))

    val system = ActorSystem(CreditCluster, config)
    system.actorOf(Props[Credit], name = "Credit")
  }

}