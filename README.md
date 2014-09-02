A [Scala](http://www.scala-lang.org/) library that contains utilities that are too small and general to warrant their own package and/or artifact, but are too large, nuanced or handy to have around to be re-implemented over and over again.


Currently this library contains a single utility:

 - [rummage.Timer](#rummage.Timer): An interface for scheduling tasks to run one or more times at some point in the future.

[source](https://github.com/zmanio/rummage) - [documentation](http://zman.io/rummage/api/#rummage.package) - [changelog](changelog/)

[![Build Status](https://travis-ci.org/zmanio/rummage.png?branch=master)](https://travis-ci.org/zmanio/rummage) [![Coverage Status](https://coveralls.io/repos/zmanio/rummage/badge.png)](https://coveralls.io/r/zmanio/rummage)


## Getting Started

Prerequisites:

 - JVM 1.5+

 - Scala 2.10.3+

To use rummage in your project simply add one line to your SBT configuration:

```scala
libraryDependencies += "io.zman" %% "rummage" % "1.0"
```

[Instructions for other build systems](http://mvnrepository.com/artifact/io.zman/rummage_2.10/1.0).

## rummage.Timer

An interface for scheduling tasks to run one or more times at some point in the future.

The [Timer](http://zman.io/rummage/api/#rummage.Timer) interface serves three primary use cases:

 - Running a task once after a specified initial delay.

 - Running a task repeatedly with the same initial and subsequent delays.

 - Running a task repeatedly with different initial and subsequent delays.

```scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import rummage.Timer

// Scheduling tasks requires an implicit `ExecutionContext`.
implicit val executionContext: ExecutionContext = ???

// The companion object is a global timer backed by a single daemon thread.
val timer: Timer = Timer

// Running a task once after a specified initial delay.
(timer after 10.seconds) { println("The future!") }

// Running a task repeatedly with the same initial and subsequent delays.
(timer every 3.minutes) { println("The future continues!") }

// Running a task repeatedly with different initial and subsequent delays.
(timer after 10.seconds andEvery 3.minutes) { println("The future never ends!") }
```

Tasks spawned by timers may be cancelled at any point in the future.

```scala
val task: Timer.Task = (timer every 10.seconds) { println("The end is near!") }
task.isCancelled // Returns false

(timer after 15.seconds) {
  task.cancel()
  task.isCancelled // Returns true
}
```

In addition to the global timer, this library provides two custom timer implementations.

```scala
import java.util.concurrent.ScheduledExecutorService
import akka.actor.{ ActorSystem, Scheduler }
import rummage.{ JavaTimer, AkkaTimer }

// Create a timer from a Java `ScheduledExecutorService`.
val executor: ScheduledExecutorService = ???
val javaTimer: Timer = JavaTimer(executor)

// Create a timer from an Akka `ActorSystem` or `Scheduler`.
val system: ActorSystem = ???
val scheduler: Scheduler = system.scheduler
val akkaTimer1: Timer = AkkaTimer(system)
val akkaTimer2: Timer = AkkaTimer(scheduler)
```

The global timer is made available as the default implicit timer instance. This means that implicit timer parameters will be assigned the global timer in absence of a user-specified implicit timer instance.

```scala
def printLater(msg: String)(implicit timer: Timer) =
  (timer after 1.minute) { println(msg) }

printLater("Triggered by the default timer.")

locally {
  implicit val localTimer: Timer = ???
  printLater("Triggered by the local implicit timer.")
}
```

For more information, see the API documentation:

 - [rummage.Timer](http://zman.io/rummage/api/#rummage.Timer)

 - [rummage.JavaTimer](http://zman.io/rummage/api/#rummage.JavaTimer)

 - [rummage.AkkaTimer](http://zman.io/rummage/api/#rummage.AkkaTimer)

