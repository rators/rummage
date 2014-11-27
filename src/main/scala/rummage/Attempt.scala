/* Attempt.scala
 * 
 * Copyright (c) 2014 zman.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rummage

import scala.collection.{ IterableLike, generic, mutable }
import scala.language.higherKinds
import scala.util.{ Try, Success => TrySuccess, Failure => TryFailure }

/**
 * The `Attempt` type represents a computation that can fail much the same way that `scala.util.Try`[T] already does.
 * However, this type extends the interface of `Try` in two important ways:
 *  - It defines three failure scenarios: `Attempt.Error` wraps an exception in the same fashion as
 *    `scala.util.Failure`, `Attempt.Problem` signals a failure not caused by an exception being thrown and
 *    `Attempt.Failures` aggregates a number of failures into a single result.
 *  - It provides projections that allow one to attempt operations across aggregates and fold the outcomes of those
 *    attempts into a single result.
 */
sealed trait Attempt[+T] { self =>

  import Attempt.{ Success, Failure, Failures }

  def isFailure: Boolean

  def isSuccess: Boolean

  def get: T

  def failed: Attempt[Failure]

  def getOrElse[U >: T](default: => U): U

  def orElse[U >: T](default: => Attempt[U]): Attempt[U]

  def toOption: Option[T]

  def foreach[U](f: T => U): Unit

  def map[U](f: T => U): Attempt[U]

  def flatMap[U](f: T => Attempt[U]): Attempt[U]

  def filter(p: T => Boolean): Attempt[T]

  def recover[U >: T](f: PartialFunction[Failure, U]): Attempt[U]

  def recoverWith[U >: T](f: PartialFunction[Failure, Attempt[U]]): Attempt[U]

  def flatten[U](implicit ev: T <:< Attempt[U]): Attempt[U]

  def transform[U](s: T => Attempt[U], f: Failure => Attempt[U]): Attempt[U]

  final def onEach[E, C[XX] <: IterableLike[XX, C[XX]]](implicit ev: T <:< C[E]): EachProjection[E, C] =
    new EachProjection

  final def onEvery[E, C[XX] <: IterableLike[XX, C[XX]]](implicit ev: T <:< C[E]): EveryProjection[E, C] =
    new EveryProjection

  /** A projection that exposes aggregations inside an attempt in a fail-fast manner. */
  final class EachProjection[E, C[XX] <: IterableLike[XX, C[XX]]] private[Attempt] (implicit ev: T <:< C[E]) {

    def map[U, That](f: E => U)(implicit cbf: generic.CanBuildFrom[C[E], U, That]): Attempt[That] =
      self flatMap { value =>
        ((Success(cbf()): Attempt[mutable.Builder[U, That]]) /: value) { (builder, element) =>
          builder flatMap { b => Attempt { f(element) } map { b += _ } }
        } map { _.result() }
      }

    def flatMap[U, That](f: E => Attempt[U])(implicit cbf: generic.CanBuildFrom[C[E], U, That]): Attempt[That] =
      self flatMap { value =>
        ((Success(cbf()): Attempt[mutable.Builder[U, That]]) /: value) { (builder, element) =>
          builder flatMap { b => (Attempt { f(element) } flatten) map { b += _ } }
        } map { _.result() }
      }

  }

  /** A projection that exposes aggregations inside an attempt in a fail-eventually manner. */
  final class EveryProjection[E, C[XX] <: IterableLike[XX, C[XX]]] private[Attempt] (implicit ev: T <:< C[E]) {

    def map[U, That](f: E => U)(implicit cbf: generic.CanBuildFrom[C[E], U, That]): Attempt[That] =
      self flatMap { value =>
        val (builder, failures) = ((cbf(), Vector[Failure]()) /: value) { (state, element) =>
          val (builder, failures) = state
          Attempt { f(element) } match {
            case Success(value) => (builder += value, failures)
            case failure: Failure => (builder, failures :+ failure)
          }
        }
        if (failures isEmpty) Success(builder.result()) else Failures(failures)
      }

    def flatMap[U, That](f: E => Attempt[U])(implicit cbf: generic.CanBuildFrom[C[E], U, That]): Attempt[That] =
      self flatMap { value =>
        val (builder, failures) = ((cbf(), Vector[Failure]()) /: value) { (state, element) =>
          val (builder, failures) = state
          Attempt { f(element) } flatten match {
            case Success(value) => (builder += value, failures)
            case failure: Failure => (builder, failures :+ failure)
          }
        }
        if (failures isEmpty) Success(builder.result()) else Failures(failures)
      }

  }

}

object Attempt {

  type Attempt[+T] = rummage.Attempt[T]

  val Attempt = this
  
  implicit def tryToAttempt[T](t: Try[T]): Attempt[T] = t map { Success(_) } recover { case e => Error(e) } get

  def apply[T](f: => T): Attempt[T] = Try { f }

  case class Success[+T](value: T) extends Attempt[T] {

    override def isFailure = false

    override def isSuccess = true

    override def get = value

    override def failed = Problem("Success.failed")

    override def getOrElse[U >: T](default: => U) = value

    override def orElse[U >: T](default: => Attempt[U]) = this

    override def toOption = Some(value)

    override def foreach[U](f: T => U) = f(value)

    override def map[U](f: T => U) = Attempt { f(value) }

    override def flatMap[U](f: T => Attempt[U]) = Attempt { f(value) } flatten

    override def filter(p: T => Boolean) =
      Attempt { if (p(value)) this else Problem(s"Predicate does not hold for $value.") } flatten

    override def recover[U >: T](f: PartialFunction[Failure, U]) = this

    override def recoverWith[U >: T](f: PartialFunction[Failure, Attempt[U]]) = this

    override def flatten[U](implicit ev: T <:< Attempt[U]) = value

    override def transform[U](s: T => Attempt[U], f: Failure => Attempt[U]) = Attempt { s(value) } flatten

  }

  sealed trait Failure extends Attempt[Nothing] {

    override def isFailure = true

    override def isSuccess = false

    override def get = throw new NoSuchElementException("Failure.get")

    override def failed = Success(this)

    override def getOrElse[U >: Nothing](default: => U) = default

    override def orElse[U >: Nothing](default: => Attempt[U]) = default

    override def toOption: Option[Nothing] = None

    override def foreach[U](f: Nothing => U) = ()

    override def map[U](f: Nothing => U) = this

    override def flatMap[U](f: Nothing => Attempt[U]) = this

    override def filter(p: Nothing => Boolean) = this

    override def recover[U >: Nothing](f: PartialFunction[Failure, U]) =
      Attempt { if (f.isDefinedAt(this)) Success(f(this)) else this } flatten

    override def recoverWith[U >: Nothing](f: PartialFunction[Failure, Attempt[U]]) =
      Attempt { f.applyOrElse(this, identity[Failure]) } flatten

    override def flatten[U](implicit ev: Nothing <:< Attempt[U]) = this

    override def transform[U](s: Nothing => Attempt[U], f: Failure => Attempt[U]) = Attempt { f(this) } flatten

  }

  case class Error(exception: Throwable) extends Failure

  case class Problem(message: String, cause: Option[Failure] = None) extends Failure

  case class Failures(failures: Vector[Failure]) extends Failure

}