resolvers += "JitPack".at("https://jitpack.io")

libraryDependencies ++=
  "com.raquo" %%% "airstream" % "0.11.1" ::
    "com.raquo" %%% "laminar" % "0.12.1" ::
    "com.raquo" %%% "waypoint" % "0.3.0" ::
    Nil

libraryDependencies ++=
  //"com.github.fdietze.scala-js-d3v4" %%% "scala-js-d3v4" % "4864742723" ::
  "com.github.michaelahlers.scala-js-d3v4" %%% "scala-js-d3v4" % "master-SNAPSHOT" ::
    "com.github.japgolly.scalacss" %%% "core" % "0.7.0" ::
    "com.github.julien-truffaut" %%% "monocle-core" % "2.1.0" ::
    "com.github.julien-truffaut" %%% "monocle-macro" % "2.1.0" ::
    //"io.circe" %%% "circe-core" % "0.14.0-M3" ::
    //"io.circe" %%% "circe-generic" % "0.14.0-M3" ::
    //"io.circe" %%% "circe-parser" % "0.14.0-M3" ::
    //"io.circe" %%% "circe-shapes" % "0.14.0-M3" ::
    "io.github.cquiroz" %%% "scala-java-time" % "2.2.0" ::
    "io.lemonlabs" %%% "scala-uri" % "3.0.0" ::
    "io.scalaland" %%% "chimney" % "0.6.1" ::
    "org.querki" %%% "jquery-facade" % "2.0" ::
    "org.scala-js" %%% "scalajs-dom" % "1.1.0" ::
    Nil

libraryDependencies ++=
  "biz.enef" %%% "slogging" % "0.6.2" ::
    "biz.enef" %%% "slogging-http" % "0.6.2" ::
    Nil

//Compile / npmDependencies ++=
//  "d3" -> "5.15" ::
//    "@types/d3" -> "5.7.2" ::
//    Nil
