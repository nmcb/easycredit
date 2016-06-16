package essent.credit.external

import akka.actor._
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import essent.credit.ClusterDefinitions
import essent.credit.core.Credit.DefaultCreditProvider


class Bank extends Actor with ActorLogging {

  import Bank._
  import context.dispatcher

  import scala.concurrent.duration._
  context.system.scheduler.schedule(1.second, 1.second, context.self, Tick)

  def receive = {
    case Tick            => log.info("Tick")
    case ReceiveTimeout  => log.info("Timeout")
    case a: Any          => log.error(a.toString)
  }
}

object Bank {

  case object Tick

  import ClusterDefinitions._
  trait BankCommand

  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$ExternalClusterRole]"))
      .withFallback(ConfigFactory.load("application"))

    val system = ActorSystem(CreditCluster, config)
    Cluster(system) registerOnMemberUp {
      val bank = system.actorOf(Props(new Bank with DefaultCreditProvider), name = BankActorName)
    }
  }

}

