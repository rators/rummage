//
// Basic project information.
//

organization := "io.zman"

name := "rummage"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.3" % "provided",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.RC1" % "test"
)

//
// Documentation site generation.
//

site.settings

site.includeScaladoc(".")

ghpages.settings

git.remoteRepo := "git@github.com:zmanio/rummage.git"
