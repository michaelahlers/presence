libraryDependencies ++=
  "com.softwaremill.macwire" %% "macros" % "2.3.7" % Provided ::
    "com.softwaremill.macwire" %% "util" % "2.3.7" % Provided ::
    Nil

libraryDependencies ++=
  "com.vmunier" %% "scalajs-scripts" % "1.1.4" ::
    Nil

libraryDependencies ++=
  //"org.webjars.bowergithub.jquery" % "jquery" % "3.5.1" ::
  //("org.webjars.bowergithub.semantic-org" % "semantic-ui-less" % "2.4.1").excludeAll(ExclusionRule().withArtifact("jquery")) ::
  //"org.webjars" % "Semantic-UI" % "2.4.1" ::
  //"org.webjars" % "semantic-ui-less" % "2.4.1" ::
  "org.webjars" % "bootstrap" % "5.0.0-beta2" ::
    "org.webjars.npm" % "bootstrap-icons" % "1.3.0" ::
    "org.webjars" % "font-awesome" % "5.15.2" ::
    "org.webjars" % "jquery" % "3.5.1" ::
    "org.webjars" %% "webjars-play" % "2.8.0-1" ::
    Nil

libraryDependencies ++=
  "biz.enef" %% "slogging" % "0.6.2" ::
    "biz.enef" %% "slogging-slf4j" % "0.6.2" ::
    "ch.qos.logback" % "logback-classic" % "1.2.3" ::
    "org.slf4j" % "slf4j-api" % "1.7.30" ::
    Nil
