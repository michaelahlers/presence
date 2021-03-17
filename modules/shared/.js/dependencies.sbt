libraryDependencies += "io.lemonlabs" %%% "scala-uri" % "3.2.0"

libraryDependencies += "org.scala-js" %%% "scalajs-java-time" % "1.0.0"

libraryDependencies ++=
  "io.circe" %%% "circe-core" % "0.13.0" ::
    "io.circe" %%% "circe-generic" % "0.13.0" ::
    "io.circe" %%% "circe-generic-extras" % "0.13.0" ::
    "io.circe" %%% "circe-parser" % "0.13.0" ::
    Nil
