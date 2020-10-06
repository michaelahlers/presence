lazy val `presence` =
  (project in file("."))
    .aggregate(
      `web-client`,
      `web-server`)

lazy val `web-client` =
  (project in file("modules") / "web-client")
    .dependsOn(sharedJs)

lazy val `web-server` =
  (project in file("modules") / "web-server")
    .settings(scalaJSProjects += `web-client`)
    .dependsOn(sharedJvm)

lazy val shared =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("modules") / "shared")
    .jsConfigure(_.enablePlugins(ScalaJSWeb))

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

ThisBuild / Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
