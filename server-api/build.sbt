import PlayGulpKeys._

fork := true

//parallelExecution in Test := false

fork in Test := true

scalaVersion := "2.11.7"

scalacOptions in ThisBuild ++=
  "-target:jvm-1.8" ::
    Nil

organization in ThisBuild := "ahlers.michael"

name := "michaelahlers-presence-server-api"

version := "git describe HEAD".!!.trim.replaceAll("-[\\w]+$", "").drop(1)

libraryDependencies ++=
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" ::
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
      buildInfoPackage := "ahlers.michael.presence.server.api"
    )

routesGenerator := InjectedRoutesGenerator

pipelineStages := Seq(digest, gzip)

/* It's preferable to not move resources in the project where this method will be applied. */
gulpDirectory <<= (baseDirectory in Compile)

herokuJdkVersion in Compile := "1.8"

herokuStack in Compile := "cedar-14"

val herokuEnvironments =
  Map(
    "qa" -> "michaelahlers-presence-qa"
  )

herokuAppName in Compile := herokuEnvironments.getOrElse(sys.props("env"), herokuEnvironments("qa"))
