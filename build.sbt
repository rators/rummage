import SiteKeys._
import GhReadmeKeys._
import GhPagesKeys.ghpagesNoJekyll
import SonatypeKeys._

//
// Basic project information.
//

name := "rummage"

version := "1.0"

description := "A collection of small Scala utilities."

homepage := Some(url("http://zman.io/rummage/"))

startYear := Some(2014)

organization := "io.zman"

organizationName := "zman.io"

organizationHomepage := Some(url("http://zman.io/"))

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.0" % "provided",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.RC1" % "test"
)

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

ScoverageSbtPlugin.instrumentSettings

CoverallsPlugin.coverallsSettings

//
// Documentation site generation.
//

site.settings

includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.md" | "*.yml"

site.includeScaladoc("api")

ghreadme.settings

readmeMappings ++= Seq(
  "." --- Seq(
    "title"   -> "rummage",
    "tagline" -> "assorted scala utilities",
    "layout"  -> "page",
    "cover"   -> "rummage.jpg"
  ),
  "changelog" --- Seq(
    "title"   -> "rummage history",
    "layout"  -> "page",
    "cover"   -> "rummage.jpg"
  )
)

ghpages.settings

ghpagesNoJekyll := false

git.remoteRepo := sys.env.get("GH_TOKEN").map(_ + "@").getOrElse("https://") + "github.com:zmanio/rummage.git"

//
// Publishing to Sonatype
//

sonatypeSettings
  
pomExtra := (
  <scm>
    <url>git@github.com:zmanio/rummage.git</url>
    <connection>scm:git:git@github.com:zmanio/rummage.git</connection>
    <developerConnection>scm:git:git@github.com:zmanio/rummage.git</developerConnection>
  </scm>
  <developers>
    <developer>
      <id>lonnie</id>
      <name>Lonnie Pryor</name>
      <url>http://zman.io</url>
    </developer>
  </developers>
)
