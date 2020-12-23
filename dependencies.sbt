ThisBuild / scalaVersion := "2.13.3"

ThisBuild / libraryDependencies ++=
  Nil

ThisBuild / libraryDependencies ++=
  "com.softwaremill.diffx" %% "diffx-scalatest" % "0.3.30" % Test ::
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.5" % Test ::
    "org.scalacheck" %% "scalacheck" % "1.14.3" % Test ::
    "org.scalamock" %% "scalamock" % "4.4.0" % Test ::
    "org.scalatest" %% "scalatest" % "3.1.4" % Test ::
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
