package ars.solr.client.conversion

import ars.precondition.RequireUtils.{requireNotBlank, requireNotNull}
import ars.solr.client.SolrDocumentMap
import ars.solr.exception.SolrException

import scala.reflect.ClassTag
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

/** Converter of the instance of [[Any]] to the instance of [[To]].
  *
  * @tparam To the destination type
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
trait
CanConvertTo[+To] {

  /**
    * Tries to convert `from` to the instance of [[To]]. If it isn't possible then [[scala.util.Failure]]
    * will be returned.
    *
    * @param from the source instance
    *
    * @throws IllegalArgumentException if `from` is `null`
    *
    * @return the destination instance or error description
    */
  def convert(from: Any): Try[To]
}

object CanConvertTo {

  /**
    * Gets the value of the required `field` from the `document`.
    *
    * Algorithm:
    * - if there's `field` in the `document` then
    *   - if `field` value is `null` then [[NullPointerException]] will be thrown
    *   - if `field` value type is `T` then it will be returned;
    *   - if `field` value type isn't `T` then it will be converted to `T` with `canConvertTo` (if possible):
    *     - if conversion to `T` is successful then converted value will be returned;
    *     - if conversion to `T` is failed then [[SolrException]] will be thrown;
    * - if there's no `field` in the `document` then
    *   - if `default` is `Some(value)` then `value` will be returned.
    *   - otherwise [[SolrException]] will be thrown;
    *
    * `default` value will be returned.
    *
    * @param field the field name
    * @param default the default value ([[DefVal]] of `T` or [[NoDefVal]])
    * @param document the document
    * @param classTag the class tag
    * @param canConvertTo the [[CanConvertTo]]. If it's `null` then the new
    *                     instance of [[NoConversionCanConvertTo]] will be used.
    *
    * @tparam T the field value type
    *
    * @throws IllegalArgumentException if `field` is blank (`null` or empty),
    *                                  or at least one of `document` or `classTag` is `null`,
    *                                  or if `default` is `null` or `DefVal` of `null`
    * @throws NullPointerException if `field` value in `document` is `null`
    * @throws SolrException if there's field in the document but its value has incorrect type
    *                       (another one than `T`) or if there's a conversion error occurred
    *
    * @return the field value (non-null)
    */
  def get[T](field: String, default: DefaultValue[T] = NoDefVal)
            (implicit document: SolrDocumentMap, classTag: ClassTag[T],
             canConvertTo: CanConvertTo[T] = null): T = {

    requireNotBlank(field, "field")
    requireNotNull(document, "document")
    requireNotNull(classTag, "classTag")
    requireDefaultArg(default)

    document.get(field) match {
      case Some(null) => throw nullFieldException(field)
      case Some(value: T) => value
      case Some(value) => convertOrException(value, nonNullCanConvertTo(canConvertTo))
      case None =>
        default match {
          case DefVal(value) => value
          case NoDefVal => throw noFieldException(field)
        }
    }
  }


  /**
    * Gets the [[Option]] value of the optional `field` from the `document`.
    *
    * Algorithm:
    * - if there's the `field` in the `document`
    *   - if `field` value is `null` then [[NullPointerException]] will be thrown
    *   - if `field` value type is `T` then [[Some]] of it will be returned;
    *   - if `field` value type isn't `T` then it will be converted to `T` with `canConvertTo` (if possible):
    *     - if conversion to `T` is successful then [[Some]] of converted value will be returned;
    *     - if conversion to `T` is failed then [[SolrException]] will be thrown;
    * - if there's no `field` in the document then [[None]] will be returned.
    *
    * @param field the field name
    * @param document the document
    * @param classTag the class tag
    * @param canConvertTo the [[CanConvertTo]]. If it's `null` then the new
    *                     instance of [[NoConversionCanConvertTo]] will be used.
    *
    * @tparam T the field value type
    *
    * @throws IllegalArgumentException if `field` is blank (`null` or empty),
    *                                  or at least one of `document` or `classTag` is `null`
    * @throws SolrException if there's field in the document but its value has incorrect type
    *                       (another one than `T`) or if there's a conversion error occurred
    *
    * @return the [[Option]] of the field value (non-null)
    */
  def opt[T](field: String)
            (implicit document: SolrDocumentMap, classTag: ClassTag[T],
             canConvertTo: CanConvertTo[T] = null): Option[T] = {

    requireNotBlank(field, "field")
    requireNotNull(document, "document")
    requireNotNull(classTag, "classTag")

    document.get(field).map {
      case null => throw nullFieldException(field)
      case value: T => value
      case value => convertOrException(value, nonNullCanConvertTo(canConvertTo))
    }
  }

  private def convertOrException[T](value: Any, canConvertTo: CanConvertTo[T])(implicit classTag: ClassTag[T]): T = {
    canConvertTo.convert(value) match {
      case Success(v) => v
      case Failure(e) => throw wrapToSolrExceptionIfNeed(e);
    }
  }

  private def requireDefaultArg[T](default: T): Unit = {
    default match { // TODO: refactoring
      case null | DefVal(null)  => requireNotNull(null, "default")
      case _ =>
    }
  }

  private def nonNullCanConvertTo[T](canConvertTo: CanConvertTo[T])(implicit classTag: ClassTag[T]): CanConvertTo[T] = {
    if(canConvertTo != null) canConvertTo else new NoConversionCanConvertTo[T]
  }

  private def nullFieldException(field: String): NullPointerException ={
    new NullPointerException(s"The value of the field '$field' is `null`")
  }

  private def wrapToSolrExceptionIfNeed(exception: Throwable): Throwable = {
    exception match {
      case _: SolrException => exception
      case NonFatal(_) => new SolrException(cause = exception)
      case _ => exception
    }
  }

  private def noFieldException(field: String): SolrException = {
    new SolrException(s"No required field '$field' in the document.")
  }
}