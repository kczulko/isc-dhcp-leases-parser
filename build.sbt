name := "isc-dhcp-leases-parser"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.0"

organization := "com.github.kczulko"
organizationName := "kczulko"
organizationHomepage := Some(new URL("https://github.com/kczulko"))
parallelExecution := true
publishArtifact in Test := false

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)