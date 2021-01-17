enablePlugins(ScalaJSBundlerPlugin)
enablePlugins(ScalaJSPlugin)
enablePlugins(ScalablyTypedConverterPlugin)
//enablePlugins(TzdbPlugin)

scalaJSUseMainModuleInitializer := true

/* Defeats interoperability with Laminar, which is married to Scala.js DOM. */
//stUseScalaJsDom := false
stEnableScalaJsDefined := Selection.All

useYarn := true

// TODO: Configure caching for Scalably Typed artifacts.

//zonesFilter := (_ == "America/New_York")
