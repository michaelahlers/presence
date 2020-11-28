enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)

Compile / compile :=
  (Compile / compile)
    .dependsOn(scalaJSPipeline)
    .value

Assets / LessKeys.less / includeFilter := "*.less"

Assets / pipelineStages ++=
  scalaJSPipeline ::
    Nil

pipelineStages ++=
  digest ::
    gzip ::
    Nil

routesImport += "ahlers.presence.web.server.WebServerRoutesImport._"
