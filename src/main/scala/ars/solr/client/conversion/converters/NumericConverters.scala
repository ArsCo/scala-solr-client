package ars.solr.client.conversion.converters

import ars.solr.client.conversion.ConverterSeqCanConvertTo
import ars.solr.client.conversion.implicits._

import scala.util.Try

/** Numeric converters.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
object NumericConverters {

  final implicit val DefaultByteConverter: ConverterSeqCanConvertTo[Byte] =
    new ConverterSeqCanConvertTo(convertToByte _)

  final implicit val DefaultShortConverter: ConverterSeqCanConvertTo[Short] =
    new ConverterSeqCanConvertTo(convertToShort _)

  final implicit val DefaultIntConverter: ConverterSeqCanConvertTo[Int] =
    new ConverterSeqCanConvertTo(convertToInt _)

  final implicit val DefaultLongConverter: ConverterSeqCanConvertTo[Long] =
    new ConverterSeqCanConvertTo(convertToLong _)

  final implicit val DefaultFloatConverter: ConverterSeqCanConvertTo[Float] =
    new ConverterSeqCanConvertTo(convertToFloat _)

  final implicit val DefaultDoubleConverter: ConverterSeqCanConvertTo[Double] =
    new ConverterSeqCanConvertTo(convertToDouble _)

  final implicit val DefaultBigIntConverter: ConverterSeqCanConvertTo[BigInt] =
    new ConverterSeqCanConvertTo(convertToBigInt _)

  final implicit val DefaultBigDecimalConverter: ConverterSeqCanConvertTo[BigDecimal] =
    new ConverterSeqCanConvertTo(convertToBigDecimal _)


  private def convertToByte(from: Any): Option[Byte] = {
    from match {
      case v: java.lang.Byte => Some(v)

      case v: String => Try(v.toByte).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  private def convertToShort(from: Any): Option[Short] = {
    from match {
      case v: java.lang.Byte => Some(v.toShort)
      case v: java.lang.Short => Some(v)

      case v: String => Try(v.toShort).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  private def convertToInt(from: Any): Option[Int] = {
    from match {
      case v: java.lang.Byte => Some(v.toInt)
      case v: java.lang.Short => Some(v.toInt)
      case v: java.lang.Integer => Some(v)

      case v: String => Try(v.toInt).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  private def convertToLong(from: Any): Option[Long] = {
    from match {
      case v: java.lang.Byte => Some(v.toLong)
      case v: java.lang.Short => Some(v.toLong)
      case v: java.lang.Integer => Some(v.toLong)
      case v: java.lang.Long => Some(v)

      case v: String => Try(v.toLong).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  private def convertToFloat(from: Any): Option[Float] = {
    from match {
      case v: java.lang.Byte => Some(v.toFloat)
      case v: java.lang.Short => Some(v.toFloat)
      case v: java.lang.Integer => Some(v.toFloat)
      case v: java.lang.Long => Some(v.toFloat)
      case v: java.lang.Float => Some(v)

      case v: String => Try(v.toFloat).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  private def convertToDouble(from: Any): Option[Double] = {
    from match {
      case v: java.lang.Byte => Some(v.toDouble)
      case v: java.lang.Short => Some(v.toDouble)
      case v: java.lang.Integer => Some(v.toDouble)
      case v: java.lang.Long => Some(v.toDouble)
      case v: java.lang.Float => Some(v.toDouble)
      case v: java.lang.Double => Some(v)

      case v: String => Try(v.toDouble).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  private def convertToBigInt(from: Any): Option[BigInt] = {
    from match {
      case v: java.lang.Byte => Some(v.toInt)
      case v: java.lang.Short => Some(v.toInt)
      case v: java.lang.Integer => Some(v.toInt)
      case v: java.lang.Long => Some(v.toLong)
      case v: java.math.BigInteger => Some(v)

      case v: String => Try(BigInt(v)).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  private def convertToBigDecimal(from: Any): Option[BigDecimal] = {
    from match {
      case v: java.lang.Byte => Some(v.toInt)
      case v: java.lang.Short => Some(v.toInt)
      case v: java.lang.Integer => Some(v.toInt)
      case v: java.lang.Long => Some(v.toLong)
      case v: java.math.BigInteger => Some(BigDecimal(v))
      case v: java.lang.Float => Some(BigDecimal(v))
      case v: java.lang.Double => Some(BigDecimal(v))
      case v: java.math.BigDecimal => Some(v)

      case v: String => Try(BigDecimal(v)).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }
}
