package essent
package credit
package external

import java.text.SimpleDateFormat
import java.util.Calendar

import akka.actor._
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import essent.credit.core.Core._

class Bank extends Actor with ActorLogging {
  this: CreditProvider =>

  import Bank._
  import scala.concurrent.duration._

  import context.dispatcher
  context.system.scheduler.schedule(1.second, 1.second, context.self, Tick)

  val router = newCreditRouter(context)

  def receive = {
    case Tick           => router.forward(Tick)
    case ReceiveTimeout => log.info("Timeout")
    case a: Any         => log.error(a.toString)
  }

   def emit: Transfer = Transfer(1.00, "666", "001", currentDate, "ledger-01")
}

object Bank {

  case object Tick

  import ClusterDefinitions._

  def main(args: Array[String]): Unit = {
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles=[$ExternalClusterRole]"))
      .withFallback(ConfigFactory.load("application"))

    val system = ActorSystem(CreditCluster, config)
    Cluster(system) registerOnMemberUp {
      system.actorOf(Props(new Bank with DefaultCreditProvider), name = BankActorName)
    }
  }

  def currentDate: Date = {
    val format = new SimpleDateFormat("yyyy-MM-dd")
    format.format(Calendar.getInstance().getTime)
  }
}

