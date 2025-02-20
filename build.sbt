// Enable plugins
enablePlugins(SbtWar)

ThisBuild / scalaVersion := "3.3.4"
ThisBuild / organization := "dev.cptlobster"

val ScalatraVersion = "3.1.1"

// project definition
lazy val root = (project in file("."))
  .settings(
    name := "cta-tracker-backend",
    version := "0.1.0-SNAPSHOT"
  )

javacOptions ++= Seq("-source", "21", "-target", "21")

// dependencies
libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra-jakarta" % ScalatraVersion,
  "org.scalatra" %% "scalatra-json-jakarta" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest-jakarta" % ScalatraVersion % "test",
  "org.json4s" %% "json4s-jackson" % "4.0.7",
  "org.json4s" %% "json4s-ext" % "4.0.7",
  "com.softwaremill.sttp.client3" %% "core" % "3.10.3",
  "com.softwaremill.sttp.client3" %% "json4s" % "3.10.3",
  "ch.qos.logback" % "logback-classic" % "1.5.6" % "runtime",
  "jakarta.servlet" % "jakarta.servlet-api" % "6.0.0" % "provided"
)

// configure tests
Test / fork := true