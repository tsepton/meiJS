import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "be.tsepton"
ThisBuild / organizationName := "tsepton"

lazy val root = (project in file("."))
  .settings(
    name := "MeiJS",
    libraryDependencies += scalaTest % Test
  )

libraryDependencies ++= List(
  "org.scala-js" %%% "scalajs-dom" % "2.1.0"
)

// ScalaJS export related
enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)
Compile / mainClass := Some("meijs.Main")
scalaJSUseMainModuleInitializer := true
webpackBundlingMode := BundlingMode.LibraryAndApplication()

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
