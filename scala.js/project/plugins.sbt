List(
  "org.scala-js"  % "sbt-scalajs"         % "1.11.0",
  "com.eed3si9n"  % "sbt-buildinfo"       % "0.11.0",
  "ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.1"
).map(addSbtPlugin)

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
