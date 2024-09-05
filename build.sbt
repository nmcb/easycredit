val ProjectName      = "credit"
val OrganisationName = "easy"
val ProjectVersion   = "0.0.1"

val ScalaVersion      = "2.12.20"
val AkkaVersion       = "2.5.0"
val ScalaTestVersion  = "3.0.1"
val ScalaCheckVersion = "1.13.4"

def common: Seq[Setting[_]] = Seq(
  organization := OrganisationName,
  version      := ProjectVersion,
  scalaVersion := ScalaVersion
)

lazy val root = (project in file("."))
  .settings( common: _*)
  .settings(
    name := ProjectName,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-cluster"  % AkkaVersion,
      "com.typesafe.akka" %% "akka-testkit"  % AkkaVersion       % "test",
      "org.scalactic"     %% "scalactic"     % ScalaTestVersion,
      "org.scalatest"     %% "scalatest"     % ScalaTestVersion  % "test",
      "org.scalacheck"    %% "scalacheck"    % ScalaCheckVersion % "test"
    )
  )
