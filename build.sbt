fork := true

//parallelExecution in Test := false

fork in Test := true

scalaVersion := "2.11.7"

scalacOptions in ThisBuild ++=
  "-target:jvm-1.8" ::
    Nil

organization in ThisBuild := "org.michaelahlers"

name := "michaelahlers-presence"

version := "git describe HEAD".!!.trim.replaceAll("-[\\w]+$", "").drop(1)

libraryDependencies ++=
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" ::
    //"org.scaldi" %% "scaldi-play" % "0.5.8" ::
    Nil

/* Testing dependencies. */
libraryDependencies ++=
  "org.specs2" %% "specs2-core" % "3.6.4" % "test" ::
    Nil

lazy val runtime =
  (project in file("."))
    .enablePlugins(BuildInfoPlugin, PlayScala)
    .disablePlugins(PlayLayoutPlugin)
    .settings(
      buildInfoKeys :=
        Seq[BuildInfoKey](
          name,
          version,
          scalaVersion,
          sbtVersion,
          "revision" -> "git rev-parse HEAD".!!.trim
        ),
      buildInfoPackage := "org.michaelahlers.presence"
    )

routesGenerator := InjectedRoutesGenerator
