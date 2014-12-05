---
title: about
headline: one-off scala utilities and tools
layout: home
---
A [Scala](http://www.scala-lang.org/) library that contains utilities that are too small and general to warrant their own package and/or artifact, but are too large, nuanced or handy to have around to be re-implemented over and over again.

This library contains the following utilities:


 - [rummage.Clock](http://zman.io/rummage/api/#rummage.Clock): a general-purpose time abstraction.

 - [rummage.TryAll](http://zman.io/rummage/api/#rummage.TryAll$): An extension to the `scala.util.Try` API to add support aggregated try operations.


[source](https://github.com/zmanio/rummage) - [documentation](http://zman.io/rummage/api/#rummage.package) - [changelog](changelog/)

[![Build Status](https://travis-ci.org/zmanio/rummage.png?branch=master)](https://travis-ci.org/zmanio/rummage) [![Coverage Status](https://coveralls.io/repos/zmanio/rummage/badge.png)](https://coveralls.io/r/zmanio/rummage)


## Getting Started

Prerequisites:

 - JVM 1.5+

 - Scala 2.10.4+

To use rummage in your project simply add one line to your SBT configuration:

```scala
libraryDependencies += "io.zman" %% "rummage" % "1.1"
```

[Instructions for other build systems](http://mvnrepository.com/artifact/io.zman/rummage_2.10/1.1).