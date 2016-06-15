package essent.credit.core

import essent.credit.Bank

object Core {
  def main(args: Array[String]): Unit = {
    ClusterMonitor.main(Seq("2551").toArray)
    Credit.main(Seq("2552").toArray)
    Bank.main(Array.empty)
  }
}
