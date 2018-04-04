package ars.solr.client.conversion

import ars.precondition.RequireUtils.requireNotNull

/** Represents default value. Instances of this class
  * are either an instance of [[DefVal]] or the object [[NoDefVal]].
  *
  * @tparam T the value type
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.5
  */
abstract sealed class DefaultValue[+T]

/** Represents existing default value of type `T`.
  *
  * @param value the value
  *
  * @tparam T the value type
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
case class DefVal[T](value: T) extends DefaultValue[T] {
  requireValue(value)

  def requireValue(value: T): Unit ={
    // TODO: Fix with library method
    value match {
      case null => requireNotNull(null, "value")
      case _ =>
    }
  }
}

/** Represents non-existent default value.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
case object NoDefVal extends DefaultValue



