ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "be.tsepton"
ThisBuild / organizationName := "tsepton"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "MeiJS",
    buildInfoPackage := "meijs",
    // Dependencies
    libraryDependencies ++= List(
      "org.scala-js"  %%% "scalajs-dom" % "2.1.0",
      "org.scalatest" %%% "scalatest"   % "3.2.14"
    ),
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      description
    )
  )
val author    = "tsepton"
scalacOptions ++= Seq("-deprecation", "-feature")

enablePlugins(JavascriptModulePlugin)

// Application compilation related
scalaJSUseMainModuleInitializer := true
Compile / webpackBundlingMode := BundlingMode.LibraryAndApplication()
Compile / mainClass := Some("meijs.MeiJS")
Compile / scalaJSLinkerConfig ~= {
  _.withModuleKind(ModuleKind.ESModule)
}

// Application testing related
// Add support for the DOM in `run` and `test`
Test / scalaJSLinkerConfig ~= {
  _.withModuleKind(ModuleKind.CommonJSModule)
}
Test / requireJsDomEnv := true
Test / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
