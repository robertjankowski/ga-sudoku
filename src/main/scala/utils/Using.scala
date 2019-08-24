package utils

import scala.util.Try

object Using {
  def using[A <: AutoCloseable, B](resource: => A)(f: A => B): Try[B] =
    Try { try f(resource) finally resource.close() }


}
