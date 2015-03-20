---
title: changelog
headline: a historical record of rummaging around
layout: page
---
## 1.2 (2015-03-20)

 - Added support for Scala 2.11 in addition to Scala 2.10.

 - Updated dependencies to support Scala 2.11.

## 1.1 (2014-12-04)

 - Added [rummage.Clock](http://zman.io/rummage/api/#rummage.Clock) as a more general-purpose time abstraction than [rummage.Timer](http://zman.io/rummage/api/#rummage.Timer).

 - Added [rummage.TryAll](http://zman.io/rummage/api/#rummage.TryAll): An extension to the `scala.util.Try` API to add support aggregated try operations.

 - Deprecated [rummage.Timer](http://zman.io/rummage/api/#rummage.Timer) in favor of the new [rummage.Clock](http://zman.io/rummage/api/#rummage.Clock) API. The timer interface will likely be rewritten in the future on top of the new clock API.

## 1.0 (2014-03-08)

Initial release including:

 - [rummage.Timer](http://zman.io/rummage/api/#rummage.Timer): An interface for scheduling tasks to run one or more times at some point in the future.