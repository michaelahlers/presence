scalaVersion := "2.13.3"
crossScalaVersions += "2.12.12"

ThisBuild / libraryDependencies ++=
  Nil

ThisBuild / libraryDependencies ++=
  "ch.qos.logback" % "logback-classic" % "1.2.3" ::
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2" ::
    Nil

ThisBuild / libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) =>
      compilerPlugin(("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.full)) ::
        Nil
    case _ =>
      Nil
  }
}
