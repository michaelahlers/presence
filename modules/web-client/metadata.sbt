enablePlugins(BuildInfoPlugin)

name := "presence-web-client"

buildInfoPackage := "ahlers.presence.web.client"
buildInfoObject := "WebClientBuildInfo"

buildInfoKeys ++=
  name ::
    version ::
    scalaVersion ::
    sbtVersion ::
    Nil

buildInfoOptions ++=
  BuildInfoOption.BuildTime ::
  Nil
