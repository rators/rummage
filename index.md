---
title: about
headline: one-off scala utilities and tools
layout: home
---
A [Scala](http://www.scala-lang.org/) library that contains utilities that are too small and general to warrant their own package and/or artifact, but are too large, nuanced or handy to have around to be re-implemented over and over again.

This library contains the following utilities:

 - [rummage.Clock](http://zman.io/rummage/api/#rummage.Clock): a general-purpose time abstraction.

 - [rummage.Deadline](http://zman.io/rummage/api/#rummage.Deadline$): an extension to `scala.concurrent.Future` that adds support for completion time limits.

 - [rummage.TryAll](http://zman.io/rummage/api/#rummage.TryAll$): an extension to `scala.util.Try` that adds support for aggregated try operations.


[code](https://github.com/zmanio/rummage) - [licence](https://github.com/zmanio/rummage/blob/master/LICENSE) - [api](http://zman.io/rummage/api/#rummage.package) - [history](changelog/)

[![Build Status](https://travis-ci.org/zmanio/rummage.png?branch=master)](https://travis-ci.org/zmanio/rummage) [![Coverage Status](https://coveralls.io/repos/zmanio/rummage/badge.png)](https://coveralls.io/r/zmanio/rummage)


## Getting Started

Prerequisites:

 - [Java](http://www.oracle.com/technetwork/java/index.html) 1.6+

 - [Scala](http://scala-lang.org/) 2.10.4+ or Scala 2.11.6+

To use from SBT, add the following to your build.sbt file:

```scala
libraryDependencies += "io.zman" %% "rummage" % "1.3"
```

For other build systems or to download the jar see [rummage in the central repository](http://mvnrepository.com/artifact/io.zman/rummage_2.11/1.3).