package essent
package credit

object ClusterDefinitions {
  val CreditCluster           = "essent"

  // core members
  val CoreClusterRole         = "core"
  val CreditActorName         = "credit"
  val ClusterMonitorActorName = "monitor"

  // external members
  val ExternalClusterRole     = "external"
  val BankActorName           = "abn-amro"

  // routing
  val CreditRouterName        = "creditRouter"

}
