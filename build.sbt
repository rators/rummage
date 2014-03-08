//
// Basic project information.
//

organization := "io.zman"

name := "rummage"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.0" % "provided",
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

//
// Work-around for a bad upstream dependency.
//

ivyXML :=
  <dependency org="org.eclipse.jetty.orbit" name="javax.servlet" rev="2.5.0.v201103041518">
    <artifact name="javax.servlet" type="orbit" ext="jar"/>
  </dependency>