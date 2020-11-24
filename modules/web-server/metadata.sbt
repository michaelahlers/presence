enablePlugins(BuildInfoPlugin)

name := "presence-web-server"

buildInfoPackage := "ahlers.presence.web.server"
buildInfoObject := "WebServerBuildInfo"

buildInfoKeys ++=
  name ::
    version ::
    scalaVersion ::
    sbtVersion ::
    Nil

buildInfoOptions ++=
  BuildInfoOption.BuildTime ::
    Nil
