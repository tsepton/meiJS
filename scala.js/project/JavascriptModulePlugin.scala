import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

import java.nio.charset.StandardCharsets
import java.nio.file.Files.copy
import java.nio.file.Paths.get
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.nio.file.{Files, Path, Paths}
import scala.language.implicitConversions

object JavascriptModulePlugin extends AutoPlugin {

  import autoImport._

  override def projectSettings: Seq[Def.Setting[Task[Unit]]] = Seq(
    generateNPMPackage := {
      val npmTargetDir: String = s"target/npm"
      val inputDir: String = "target/scala-2.13/meijs-opt"
      val targetDir: String = s"$npmTargetDir/src"

      def copyFiles(): Unit = {

        implicit def toPath(filename: String): Path = get(filename)

        new File(targetDir).mkdirs()

        val fileDist = List("main.js") // TODO
        for (file <- fileDist) {
          println(s"Copying file $inputDir/$file to destination...")
          copy(s"$inputDir/$file", s"$targetDir/$file", REPLACE_EXISTING)
        }

        println(f"Creating $npmTargetDir/package.json...")
        Files.write(
          Paths.get(s"$npmTargetDir/package.json"),
          packageJson.getBytes(StandardCharsets.UTF_8)
        )

        println(s"NPM package created in $npmTargetDir")
      }

      def packageJson: String = f"""
          |{
           |  "name": "@${Keys.organizationName.value}/MeiJS",
           |  "version": "${Keys.version.value}",
           |  "description": "${Keys.description.value}}",
           |  "scripts": {
           |    "test": "sbt test"
           |  },
           |  "main": "src",
           |  "repository": {
           |    "type": "git",
           |    "url": "git+https://...git"
           |  },
           |  "keywords": [
           |    "multimodal interactions"
           |  ],
           |  "author": "${Keys.organizationName.value}",
           |  "license": "SEE LICENSE IN licence.txt",
           |  "bugs": {
           |    "url": "https://..."
           |  },
           |  "homepage": "https://...",
           |  "dependencies": {}
           |}""".stripMargin

      // Compile scalaJS code
      (Compile / fullOptJS).value
      // create npm package to be published
      copyFiles()
    }
  )

  object autoImport {
    val generateNPMPackage =
      taskKey[Unit](s"Create the npm files to be published")
  }

}
