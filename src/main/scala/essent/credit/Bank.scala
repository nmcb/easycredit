package essent
package credit

import akka.actor._
import akka.routing.FromConfig
import akka.util.Timeout
import com.typesafe.config.ConfigFactory


trait BankAdaptorRouterProvider {
  def newBankAdaptorRouter(context: ActorContext): ActorRef
}

trait DefaultBankAdaptorRouterProvider extends BankAdaptorRouterProvider {
  import ClusterDefinitions._
  def newBankAdaptorRouter(context: ActorContext) = context.actorOf(
    FromConfig.props(), name = BankAdaptorRouterName
  )
}


class Bank extends Actor with ActorLogging {
  this: BankAdaptorRouterProvider =>

  import Bank._
  import scala.concurrent.duration._

  case object Tick
  import context.dispatcher
  context.system.scheduler.schedule(1.second, 1.second, context.self, Tick)


  val router = newBankAdaptorRouter(context)

  def receive = {
    case e: BankCommand   => router.forward(e)
    case ReceiveTimeout   => log.info("Timeout")
    case a: Any           => log.info(a.toString)
  }
}

object Bank {
  import ClusterDefinitions._
  trait BankCommand

  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$ExternalRole]"))
      .withFallback(ConfigFactory.load("application"))

    val system = ActorSystem(CreditCluster, config)
    system.actorOf(Props(new Bank with DefaultBankAdaptorRouterProvider), name = BankAdaptorActorName)
  }

}

