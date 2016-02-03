name := "cbdumper"

organization := "com.vpon"

version := "1.0.0"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.3.0",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "com.sandinh" %% "couchbase-akka-extension" % "3.1.6",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
)

initialCommands := "import example._"
