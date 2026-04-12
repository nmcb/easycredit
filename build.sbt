val ProjectName      = "credit"
val OrganisationName = "easy"
val ProjectVersion   = "0.0.1"

val AkkaVersion          = "2.5.32"
val ScalaTestVersion     = "3.2.20"
val ScalaCheckVersion    = "1.19.0"
val ScalaTestPlusVersion = "3.2.19.0"

ThisBuild / scalaVersion   := "2.13.18"
ThisBuild / organization   := "nmcb"
ThisBuild / version        := "0.0.1"
ThisBuild / fork           := true
ThisBuild / javaOptions    := Seq("-Xss1M")

ThisBuild / scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-feature",
  "-language:implicitConversions",
  "-language:existentials",
  "-unchecked",
  "-Werror",
  "-deprecation"
)

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val root = (project in file("."))
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
