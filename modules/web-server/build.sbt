import com.typesafe.sbt.digest.Import.DigestKeys.{ indexPath, indexWriter }

enablePlugins(PlayScala)
enablePlugins(WebScalaJSBundlerPlugin)

disablePlugins(PlayLayoutPlugin)

Compile / compile :=
  (Compile / compile)
    .dependsOn(scalaJSPipeline)
    .value

//Assets / LessKeys.less / includeFilter := "*.less"

Assets / pipelineStages ++=
  scalaJSPipeline ::
    Nil

pipelineStages ++=
  digest ::
    gzip ::
    Nil

routesImport += "ahlers.presence.web.server.WebServerRoutesImport._"

indexPath := Some("javascripts/versioned.js")
indexWriter ~= (writer => index => s"var versioned = ${writer(index)};")

Compile / herokuAppName :=
  Map(
    ("preview", "michaelahlers-presence-stage"),
    ("public", "michaelahlers-presence-public"))
    .apply(sys.props.getOrElse("environment", "stage"))

Compile / herokuSkipSubProjects := false
Compile / herokuJdkVersion := "15"
