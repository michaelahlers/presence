libraryDependencies ++=
  "com.thoughtworks.binding" %%% "futurebinding" % "12.0.0" ::
    "org.lrng.binding" %%% "html" % "1.0.3" ::
    Nil

libraryDependencies ++=
  "be.doeraene" %%% "scalajs-jquery" % "1.0.0" ::
    "org.scala-js" %%% "scalajs-dom" % "1.1.0" ::
    Nil

libraryDependencies ++=
  "biz.enef" %%% "slogging" % "0.6.2" ::
    "biz.enef" %%% "slogging-http" % "0.6.2" ::
    Nil

jsDependencies ++=
  ("org.webjars" % "jquery" % "3.5.1" / "jquery.js").minified("jquery.min.js") ::
    ("org.webjars" % "Semantic-UI" % "2.4.1" / "semantic.js").minified("semantic.min.js").dependsOn("jquery.js") ::
    Nil
