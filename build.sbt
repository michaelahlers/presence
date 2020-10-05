lazy val `presence` =
  (project in file("."))
    .aggregate()
      //`web`)

//lazy val `web` = project in file("modules") / "web"

ThisBuild / Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
