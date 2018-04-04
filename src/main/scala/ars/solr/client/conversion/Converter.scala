package ars.solr.client.conversion

import ars.precondition.RequireUtils.requireNotNull

/** Converter from instance of class [[From]] to the instance of class [[To]].
  *
  * @tparam From the source type
  * @tparam To the destination type
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
trait Converter[-From, +To] {

  /**
    * Converts the source instance `from` to the destination instance
    *
    * @param from the source instance
    *
    * @throws IllegalArgumentException if `from` is `null`
    *
    * @return the destination instance
    */
  def convert(from: From): Option[To]

  /**
    * Combines sequentially this converter with another one.
    * The resulting converter is `this` converter with prepending `converter` converter.
    * This means that it tries to convert `from` with `converter` and then, if it returns [[None]], try to convert
    * `from` with `this` converter.
    *
    * '''Example:'''
    * {{{
    *   val a: Converter[Int, String] = ...
    *   val b: Converter[Int, String] = ...
    *   val c: Converter[Int, String] = a ~: b
    *   c.convert(5) // calls a.convert() first and then b.convert()
    * }}}
    *
    * @param converter the prepending converter
    *
    * @tparam From1 the 'from' type of `converter`
    * @tparam To1 the 'to' type of `converter`
    * @tparam FromR the resulting 'from' type
    * @tparam ToR the resulting 'to' type
    *
    * @throws IllegalArgumentException if `from` is `null`
    *
    * @return the new instance of converter
    */
  def ~:[From1, To1, FromR <: From with From1, ToR >: To1](converter: Converter[From1, To1]): Converter[FromR, ToR] = {
    requireNotNull(converter, "converter")

    new Converter[FromR, ToR] {
      override def convert(from: FromR): Option[ToR] = {
        converter.convert(from).orElse(this.convert(from))
      }
    }
  }
}
