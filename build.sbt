import SonatypeKeys._

//
// Basic project information.
//

name := "rummage"

version := "1.4"

description := "One-off Scala utilities and tools."

homepage := Some(url("http://zman.io/rummage/"))

startYear := Some(2014)

organization := "io.zman"

organizationName := "zman.io"

organizationHomepage := Some(url("http://zman.io/"))

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.10.4", "2.11.8")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.0" % "provided",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % "test"
)

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

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
