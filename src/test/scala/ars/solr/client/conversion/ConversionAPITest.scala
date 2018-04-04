package ars.solr.client.conversion

import org.scalatest.FreeSpec

import scala.util.Try

/**
  *
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.0
  */
class ConversionAPITest extends FreeSpec {

  private def firstTry(from: Any): Option[Float] = {
    from match {
      case v: java.lang.Integer => Some(v.toFloat)
      case _ => None
    }
  }

  private def secondTry(from: Any): Option[Float] = {
    from match {
      case v: java.lang.Float => Some(v.toFloat)
      case _ => None
    }
  }

  private def thirdTry(from: Any): Option[Float] = {
    from match {
      case v: String => Try(v.toFloat).toOption
      case _ => None
    }
  }

  private def forthTry(from: Any): Option[Float] = {
    from match {
      case v: Boolean => if (v) Some(1) else Some(0)
      case _ => None
    }
  }


  private def convertToFloat(from: Any): Option[Float] = {
    from match {
      case v: java.lang.Long => Some(v.toFloat)
      case v: java.lang.Float => Some(v)

      case v: String => Try(v.toFloat).toOption
      case v: Boolean => if (v) Some(1) else Some(0)

      case _ => None
    }
  }

  "Conversion API" - {
    "must be consistent" in {
//      val forthConverter: Converter[Any, Float] = forthTry _
//      val thirdConverter = thirdTry _ ~: forthConverter
//
//      val secondConverter: Converter[Any, Float] = secondTry _
//      val firstConverter = firstTry _ ~: secondConverter


    }
  }
}
