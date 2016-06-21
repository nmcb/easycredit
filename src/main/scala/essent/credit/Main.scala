package essent.credit

import essent.credit.core.{ClusterMonitor, Credit}
import essent.credit.external.Bank

object Main {
  import ClusterDefinitions._

  val allClusterRoles = Array(CoreClusterRole, ExternalClusterRole)

  def main(args: Array[String]): Unit = {
    val roles = if (args.isEmpty) allClusterRoles else args

    roles foreach { role =>
      if (role == CoreClusterRole) {
        ClusterMonitor.main(Seq("2551").toArray)
        Credit.main(Seq("2552").toArray)
      }
      else if (role == ExternalClusterRole) {
        Bank.main(Array.empty)
      }
      else {
        println(s"Unknown cluster role: $role")
        System.exit(-1)
      }
    }
  }
}
