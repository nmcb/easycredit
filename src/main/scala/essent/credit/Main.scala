package essent.credit

import essent.credit.core.{ClusterMonitor, Credit}
import essent.credit.external.Bank

object Main {
  import ClusterDefinitions._

  val defaultClusterRoles = Array(CoreClusterRole, ExternalClusterRole)

  def main(args: Array[String]): Unit = {

    val roles = if (args.isEmpty) defaultClusterRoles else args

    if (roles contains CoreClusterRole)
      startCore()
    if (roles contains ExternalClusterRole)
      startExternal()
  }

  def startCore(): Unit = {
    ClusterMonitor.main(Seq("2551").toArray)
    Credit.main(Seq("2552").toArray)
  }

  def startExternal(): Unit = {
    Bank.main(Array.empty)
  }
}
