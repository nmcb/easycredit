val ProjectName      = "credit"
val OrganisationName = "easy"
val ProjectVersion   = "0.0.1"

val ScalaVersion         = "2.13.16"
val AkkaVersion          = "2.5.32"
val ScalaTestVersion     = "3.2.19"
val ScalaCheckVersion    = "1.18.1"
val ScalaTestPlusVersion = "3.2.19.0"

def common: Seq[Setting[_]] = Seq(
  organization := OrganisationName,
  version      := ProjectVersion,
  scalaVersion := ScalaVersion
)

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val root = (project in file("."))
  .settings( common: _*)
  .settings(
    name := ProjectName,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-cluster"    % AkkaVersion,
      "com.typesafe.akka" %% "akka-testkit"    % AkkaVersion          % "test",
      "org.scalactic"     %% "scalactic"       % ScalaTestVersion,
      "org.scalatest"     %% "scalatest"       % ScalaTestVersion     % "test",
      "org.scalacheck"    %% "scalacheck"      % ScalaCheckVersion    % "test",
      "org.scalatestplus" %% "scalacheck-1-18" % ScalaTestPlusVersion % "test"
    )
  )
