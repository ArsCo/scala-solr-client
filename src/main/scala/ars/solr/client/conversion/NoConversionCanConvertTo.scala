package ars.solr.client.conversion

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/** No-operation implementation of [[CanConvertTo]].
  *
  * @tparam To the destination type
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
final class NoConversionCanConvertTo[To](implicit classTag: ClassTag[To]) extends AbstractCanConvertTo[To] {

  /**
    * Returns [[Success]] of `from` if `from` is instance of `T` and [[Failure]]
    * of [[SolrConversionException]] otherwise.
    *
    * @param from the source instance
    *
    * @return the destination instance or error description
    */
  override protected[conversion] def tryConvert(from: Any): Try[To] = {
    from match {
      case v: To => Success(v)
      case _ => failure(from)
    }
  }

  private def failure(from: Any): Failure[To] = {
    val className = classTag.runtimeClass.getCanonicalName
    Failure(new SolrConversionException(s"Conversion value '$from' is not instance of '$className'"))
  }
}
