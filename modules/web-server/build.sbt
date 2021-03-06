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
    ("preview", "michaelahlers-presence-preview"),
    ("public", "michaelahlers-presence-public"))
    .apply(sys.props.getOrElse("stage", "preview"))

Compile / herokuSkipSubProjects := false
Compile / herokuJdkVersion := "15"

Compile / doc / sources := Nil
Test / doc / sources := Nil
//Global / packageDoc / publishArtifact := false
