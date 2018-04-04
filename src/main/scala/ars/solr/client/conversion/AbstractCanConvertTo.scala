package ars.solr.client.conversion

import ars.precondition.RequireUtils._

import scala.reflect.ClassTag
import scala.util.{Success, Try}

/** Abstract implementation of [[CanConvertTo]].
  *
  * @param classTag the destination class tag.
  *
  * @tparam To the destination type
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
abstract class AbstractCanConvertTo[To](implicit classTag: ClassTag[To]) extends CanConvertTo[To] {

  /**
    * It validates `from` and if it's valid and is not already instance of [[To]]
    * then invokes [[tryConvert]] method.
    *
    * @param from the source instance
    *
    * @throws IllegalArgumentException if `from` is `null`
    *
    * @return the destination instance or error description
    */
  override def convert(from: Any): Try[To] = {
    requireConvertArg(from)

    from match {
      case v: To => Success(v)
      case _ => tryConvert(from)
    }
  }

  // TODO: Fix -> To library method
  protected def requireConvertArg(from: Any): Unit = {
    from match {
      case null => requireNotNull(null, "from")
      case _ =>
    }
  }

  /**
    * Must be overridden to try to convert `from`.
    * It's recommended to use [[SolrConversionException]] if error occured.
    *
    * @param from the source instance
    *
    * @return the destination instance or error description
    */
  protected[conversion] def tryConvert(from: Any): Try[To]
}
