name := "presence"
description := "Personal web presence for Michael Ahlers."

ThisBuild / organization := "consulting.ahlers"
ThisBuild / organizationName := "Ahlers Consulting"

ThisBuild / homepage := "http://ahlers.consulting"
ThisBuild / startYear := Some(2020)

ThisBuild / developers :=
  Developer("michaelahlers", "Michael Ahlers", "michael@ahlers.consulting", url("http://github.com/michaelahlers")) ::
    Nil

ThisBuild / scmInfo :=
  Some(
    ScmInfo(
      browseUrl = url("https://github.com/michaelahlers/michaelahlers-presence"),
      connection = "https://github.com/michaelahlers/michaelahlers-presence.git",
      devConnection = Some("git@github.com:michaelahlers/michaelahlers-presence.git")
    ))

ThisBuild / licenses += "MIT" -> new URL("http://opensource.org/licenses/MIT")
