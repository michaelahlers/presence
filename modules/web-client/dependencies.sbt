libraryDependencies ++=
  "com.thoughtworks.binding" %%% "futurebinding" % "latest.release" ::
    "org.lrng.binding" %%% "html" % "latest.release" ::
    Nil

libraryDependencies ++=
  "org.scala-js" %%% "scalajs-dom" % "1.1.0" ::
    Nil

libraryDependencies ++=
  "biz.enef" %%% "slogging" % "0.6.2" ::
    "biz.enef" %%% "slogging-http" % "0.6.2" ::
    Nil

jsDependencies ++=
  ("org.webjars" % "jquery" % "2.1.3" / "jquery.js").minified("jquery.min.js") ::
    ("org.webjars" % "Semantic-UI" % "2.4.0" / "semantic.js").minified("semantic.min.js").dependsOn("jquery.js") ::
    Nil
