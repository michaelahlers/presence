addCompilerPlugin(("org.scalamacros" %% "paradise" % "2.1.1").cross(CrossVersion.full))

//scalacOptions --=
//  "-Wunused:implicits" ::
//    "-Wunused:imports" ::
//    "-Wunused:params" ::
//    "-Ywarn-unused-import" ::
//    "-Ywarn-unused:imports" ::
//    "-Ywarn-unused:implicits" ::
//    "-Ywarn-unused:locals" ::
//    "-Ywarn-unused:params" ::
//    "-Ywarn-unused:privates" ::
//    Nil

ThisBuild / scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) =>
      "-Ypartial-unification" ::
        Nil
    case _ =>
      Nil
  }
}
