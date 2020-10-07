libraryDependencies ++=
  "com.softwaremill.macwire" %% "macros" % "2.3.6" % Provided ::
    Nil

libraryDependencies ++=
  "com.vmunier" %% "scalajs-scripts" % "1.1.4" ::
    Nil

libraryDependencies ++=
  "biz.enef" %% "slogging" % "0.6.2" ::
    "biz.enef" %% "slogging-slf4j" % "0.6.2" ::
    "ch.qos.logback" % "logback-classic" % "1.2.3" ::
    "org.slf4j" % "slf4j-api" % "1.7.30" ::
    Nil
