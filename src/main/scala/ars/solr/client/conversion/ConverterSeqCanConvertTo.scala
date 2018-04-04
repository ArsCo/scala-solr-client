package ars.solr.client.conversion

import ars.precondition.RequireUtils
import ars.precondition.RequireUtils.{requireAllNotNull, requireNotBlank, requireNotNull}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

/** Implementation of [[CanConvertTo]] that uses underlying sequence of converters [[Converter]].
  *
  * @param converters the converters
  * @param classTag the destination class tag.
  *
  * @tparam To the destination type
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
class ConverterSeqCanConvertTo[To](val converters: Seq[Converter[Any, To]])
                                  (implicit classTag: ClassTag[To]) extends AbstractCanConvertTo[To] {

  requireNotBlank(converters, "converters")


  /**
    * Creates single converter [[CanConvertTo]].
    *
    * @param converter the converter
    * @param classTag the class tag
    *
    * @return the new instance of [[ConverterSeqCanConvertTo]]
    */
  def this(converter: Converter[Any, To])(implicit classTag: ClassTag[To]) = this(Seq(converter))

  /**
    * Creates new instance of [[ConverterSeqCanConvertTo]] by concatenating inverted sequence of `preConverters`
    * and `this.converters`.
    *
    * @param preConverters prefix converters
    *
    * @return the new instance of [[ConverterSeqCanConvertTo]]
    */
  def ~(preConverters: Seq[Converter[Any, To]]): ConverterSeqCanConvertTo[To] = {
    requireNotBlank(preConverters, "preConverters")
    requireAllNotNull(preConverters, "preConverters")

    new ConverterSeqCanConvertTo[To](preConverters.reverse ++ this.converters)
  }

  /**
    * Creates new instance of [[ConverterSeqCanConvertTo]] by concatenating `preConverter`
    * and `this.converters`.
    *
    * @param preConverter prefix converters
    *
    * @return the new instance of [[ConverterSeqCanConvertTo]]
    */
  def ~(preConverter: Converter[Any, To]): ConverterSeqCanConvertTo[To] = {
    requireNotNull(preConverter, "preConverter")

    new ConverterSeqCanConvertTo[To](preConverter +: this.converters)
  }

  protected[conversion] override def tryConvert(from: Any): Try[To] = {
    converters.view
      .map(_.convert(from))
      .find(_.isDefined)
      .map(v => Success(v.get))
      .getOrElse(conversionFailure(from))
  }

  protected def conversionFailure(from: Any): Failure[To] = {
    Failure(
      new Exception(
        s"Can't convert value '$from' of type '${from.getClass.getCanonicalName}' to ${classTag.runtimeClass}."))
  }
}
