import com.typesafe.sbt.packager.docker.ExecCmd

name := "ga-sudoku"

version := "0.1"

scalaVersion := "2.12.8"


scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

libraryDependencies ++= Seq(
  "ch.qos.logback"             %  "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2",
  "org.scala-lang.modules"     %% "scala-swing"     % "2.1.1",
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

mainClass in Compile := Some("Boot")

dockerBaseImage := "birchwoodlangham/x11-scala-dev-openjdk-8"
dockerCommands += ExecCmd("CMD", "sbt", "run")
dockerRepository := Some("robjankowski")