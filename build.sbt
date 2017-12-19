import sbt.Attributed

name := """eon-demo-scala-play"""

version := "0.22"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(guice,ws,
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test")

evictionWarningOptions in update := EvictionWarningOptions.default
  .withWarnTransitiveEvictions(false)
  .withWarnDirectEvictions(false)

scalaVersion := "2.12.3"

mainClass in assembly := Some("play.core.server.ProdServerStart")

test in assembly := {}
parallelExecution in Test := false
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case "META-INF/io.netty.versions.properties" => MergeStrategy.concat
  case "META-INF/services/com.fasterxml.jackson.databind.Module" => MergeStrategy.first
  case x if x.startsWith("META-INF/javamail") => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case x if x.startsWith("com/sun/mail/") => MergeStrategy.last
  case x if x.startsWith("javax/mail/" +
    "") => MergeStrategy.last
  case x if x.startsWith("org/apache/commons/logging") => MergeStrategy.first
  case x if x.startsWith("play/api/libs/ws/package") => MergeStrategy.first
  case x if x.contains("play/reference-overrides.conf") => MergeStrategy.concat
  case x if x.contains("application.conf") => MergeStrategy.concat
  case x if x.contains("routes") => MergeStrategy.first
  case x if x.contains("Routes") => MergeStrategy.first
  case x if x.contains("Module") => MergeStrategy.first
  case x =>
    val oldStrategy = (mergeStrategy in assembly).value
    oldStrategy(x)
}