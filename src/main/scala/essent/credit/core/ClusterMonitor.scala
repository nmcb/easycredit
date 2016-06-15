package essent.credit.core

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.typesafe.config.ConfigFactory
import essent.credit.ClusterDefinitions._

class ClusterMonitor extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  def receive = {
    case MemberUp(member)                      => log.info("Member is Up: {}", member.address)
    case UnreachableMember(member)             => log.info("Member is Unreachable: {}", member)
    case MemberRemoved(member, previousStatus) => log.info("Member is Removed: {} after {}", member.address, previousStatus)
    case _: MemberEvent                        => // ignore
  }
}


object ClusterMonitor {

  def main(args: Array[String]): Unit = {

    val port = if (args.isEmpty) "0" else args(0)

    val config = ConfigFactory
      .parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$CoreRole]"))
      .withFallback(ConfigFactory.load("application"))

    val system = ActorSystem(CreditCluster, config)
    system.actorOf(Props[ClusterMonitor], name = s"monitor-$port")
  }

}