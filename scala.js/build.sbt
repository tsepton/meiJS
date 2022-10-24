import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "be.tsepton"
ThisBuild / organizationName := "tsepton"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  //.enablePlugins(ScalaJSBundlerPlugin)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "MeiJS",
    libraryDependencies += scalaTest % Test,
    buildInfoPackage := "meijs",
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      description
    )
  )
val author = "tsepton"

// Dependencies
libraryDependencies ++= List(
  "org.scala-js" %%% "scalajs-dom" % "2.1.0"
)

// ScalaJS RELATED
Compile / mainClass := Some("meijs.Main")
scalaJSUseMainModuleInitializer := true
webpackBundlingMode := BundlingMode.LibraryAndApplication()
scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }

// Tasks related
// NPM package generation
enablePlugins(JavascriptModulePlugin)
