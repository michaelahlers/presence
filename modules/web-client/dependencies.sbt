resolvers += "JitPack".at("https://jitpack.io")

libraryDependencies ++=
  "com.raquo" %%% "airstream" % "0.11.1" ::
    "com.raquo" %%% "laminar" % "0.11.0" ::
    "com.raquo" %%% "waypoint" % "0.2.0" ::
    Nil

libraryDependencies ++=
  //"com.github.fdietze.scala-js-d3v4" %%% "scala-js-d3v4" % "af70ae6" ::
  "com.github.japgolly.scalacss" %%% "core" % "0.6.1" ::
    "com.github.julien-truffaut" %%% "monocle-core" % "2.1.0" ::
    "com.github.julien-truffaut" %%% "monocle-macro" % "2.1.0" ::
    "io.circe" %%% "circe-core" % "0.14.0-M3" ::
    "io.circe" %%% "circe-generic" % "0.14.0-M3" ::
    "io.circe" %%% "circe-parser" % "0.14.0-M3" ::
    "io.circe" %%% "circe-shapes" % "0.14.0-M2" ::
    "io.github.cquiroz" %%% "scala-java-time" % "2.1.0" ::
    "io.scalaland" %%% "chimney" % "0.6.1" ::
    "org.querki" %%% "jquery-facade" % "2.0" ::
    "org.scala-js" %%% "scalajs-dom" % "1.1.0" ::
    Nil

libraryDependencies ++=
  "biz.enef" %%% "slogging" % "0.6.2" ::
    "biz.enef" %%% "slogging-http" % "0.6.2" ::
    Nil

Compile / npmDependencies ++=
  "d3" -> "6.3" ::
    "@types/d3" -> "6.2" ::
    Nil
