name := "ga-sudoku"

version := "0.1"

scalaVersion := "2.13.0"


scalacOptions ++= Seq(
  "-encoding", "utf8", // Option and arguments on same line
  "-Xfatal-warnings", // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"