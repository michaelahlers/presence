libraryDependencies ++=
  "com.softwaremill.macwire" %% "macros" % "2.3.7" % Provided ::
    "com.softwaremill.macwire" %% "util" % "2.3.7" % Provided ::
    Nil

libraryDependencies +=
  "com.github.pathikrit" %% "better-files" % "3.9.1"

libraryDependencies +=
  "com.vmunier" %% "scalajs-scripts" % "1.1.4"

libraryDependencies ++=
  "io.circe" %% "circe-yaml" % "0.13.1" ::
    Nil

libraryDependencies ++=
  "com.github.julien-truffaut" %% "monocle-core" % "3.0.0-M1" ::
    "com.github.julien-truffaut" %% "monocle-macro" % "3.0.0-M1" ::
    Nil

libraryDependencies ++=
  "org.webjars" % "bootstrap" % "5.0.0-beta2" ::
    "org.webjars.npm" % "bootstrap-icons" % "1.4.0" ::
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
