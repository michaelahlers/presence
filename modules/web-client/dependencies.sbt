libraryDependencies ++=
  "com.raquo" %%% "airstream" % "0.11.1" ::
    "com.raquo" %%% "laminar" % "0.11.0" ::
    "com.raquo" %%% "waypoint" % "0.2.0" ::
    //"com.thoughtworks.binding" %%% "route" % "12.0.0" ::
    //"com.thoughtworks.binding" %%% "futurebinding" % "12.0.0" ::
    //"org.lrng.binding" %%% "html" % "1.0.3" ::
    Nil

libraryDependencies ++=
  "com.github.japgolly.scalacss" %%% "core" % "0.6.1" ::
    //"com.github.japgolly.scalajs-react" %%% "extra" % "1.7.7" ::
    "com.github.julien-truffaut" %%% "monocle-core" % "2.0.3" ::
    "com.github.julien-truffaut" %%% "monocle-macro" % "2.0.3" ::
    "io.circe" %%% "circe-core" % "0.14.0-M2" ::
    "io.circe" %%% "circe-generic" % "0.14.0-M2" ::
    "io.circe" %%% "circe-parser" % "0.14.0-M2" ::
    "org.querki" %%% "jquery-facade" % "2.0" ::
    "org.scala-js" %%% "scalajs-dom" % "1.1.0" ::
    //"tech.sparse" %%% "trail" % "0.3.0" ::
    Nil

libraryDependencies ++=
  "biz.enef" %%% "slogging" % "0.6.2" ::
    "biz.enef" %%% "slogging-http" % "0.6.2" ::
    Nil

jsDependencies ++=
  ("org.webjars" % "jquery" % "3.5.1" / "jquery.js").minified("jquery.min.js") ::
    ("org.webjars" % "Semantic-UI" % "2.4.1" / "semantic.js").minified("semantic.min.js").dependsOn("jquery.js") ::
    Nil
