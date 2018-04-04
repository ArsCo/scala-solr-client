package ars.solr.client.conversion

import scala.util.Try
import scala.language.implicitConversions

/** Implicit conversions for package `ars.solr.client.conversion`
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
package object implicits {
  implicit def toCanConvertTo[To](c: (Any) => Try[To]): CanConvertTo[To] = {
    new CanConvertTo[To] {
      override def convert(from: Any): Try[To] = c(from)
    }
  }

  implicit def toConverter[From, To](c: (From) => Option[To]): Converter[From, To] = {
    new Converter[From, To] {
      override def convert(from: From): Option[To] = c(from)
    }
  }

  implicit def any2defaultValue[T](v: T): DefaultValue[T] = if (v == null) NoDefVal else DefVal(v)
}
