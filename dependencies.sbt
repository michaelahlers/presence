scalaVersion := "2.13.3"

ThisBuild / libraryDependencies ++=
    Nil

ThisBuild / libraryDependencies ++=
  "ch.qos.logback" % "logback-classic" % "1.2.3" ::
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2" ::
    Nil
