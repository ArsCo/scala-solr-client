package ars.solr.client.conversion.converters

import java.time.{Instant, LocalDate, LocalDateTime, LocalTime}

import ars.solr.client.conversion.ConverterSeqCanConvertTo
import ars.solr.client.conversion.implicits._

/** Converters for `java.time` classes.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
object JavaTimeConverters {

  final implicit val DefaultInstantConverter: ConverterSeqCanConvertTo[Instant] =
    new ConverterSeqCanConvertTo(convertToInstant _)

  final implicit val DefaultLocalDateConverter: ConverterSeqCanConvertTo[LocalDate] =
    new ConverterSeqCanConvertTo(convertToLocalDate _)

  final implicit val DefaultLocalTimeConverter: ConverterSeqCanConvertTo[LocalTime] =
    new ConverterSeqCanConvertTo(convertToLocalTime _)

  final implicit val DefaultLocalDateTimeConverter: ConverterSeqCanConvertTo[LocalDateTime] =
    new ConverterSeqCanConvertTo(convertToLocalDateTime _)

  private def convertToInstant(from: Any): Option[Instant] = {
    from match {
      case v: java.util.Date => Some(v.toInstant)
      case _ => None
    }
  }

  private def convertToLocalDate(from: Any): Option[LocalDate] = {
    from match {
      case v: java.util.Date =>  Some(LocalDate.from(v.toInstant))
      case _ => None
    }
  }

  private def convertToLocalTime(from: Any): Option[LocalTime] = {
    from match {
      case v: java.util.Date =>  Some(LocalTime.from(v.toInstant))
      case _ => None
    }
  }

  private def convertToLocalDateTime(from: Any): Option[LocalDateTime] = {
    from match {
      case v: java.util.Date =>  Some(LocalDateTime.from(v.toInstant))
      case _ => None
    }
  }
}
