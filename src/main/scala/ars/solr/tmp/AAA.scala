package ars.solr.tmp

import java.lang

import ars.solr.client.SolrDocumentMap
import ars.solr.client.conversion.converters.NumericConverters
import ars.solr.client.conversion.{CanConvertTo, NoConversionCanConvertTo}


//class CanConvertToInt extends AbstractCanConvert[Int] {
//
//  protected override def _convertTo(from: Any): Option[Int] = {
//    from match {
//      case v: java.lang.Integer => Some(v.toInt)
//      case _ => None
//    }
//  }
//}



//class CanConvertToLocalDateTime extends AbstractCanConvert[LocalDateTime] {
//  override def _convertTo(from: Any): Option[LocalDateTime = {
//    from match {
//      case v: java.util.Date =>
//        v.toInstant.atOffset(UTC).toLocalDateTime // TODO: Bad implementation
//    }
//  }
//}

























object MainTest {
  def main(args: Array[String]): Unit = {
    val ai = NumericConverters.DefaultIntConverter
    println("2 -> ", ai.convert(2))
    println("4 (Integer)" -> ai.convert(new Integer(4)))
    //    print("sadfsdf (String)" -> ai.convertTo("sadfsdf"))

    val eai = NumericConverters.DefaultDoubleConverter
    println("2.56 -> ", eai.convert(2.56))
    println("4.5 (Double)" -> eai.convert(new lang.Double(4.5)))
    //    print("sadfsdf (String)" -> eai.convertTo("sadfsdf"))


    //    val n = DefaultConverters.NumberConverter
    //    println("2 -> ", n.convert(2))
    //    println("4 (Integer)" -> n.convert(new Integer(4)))
    //    println("2.56 -> ", n.convert(2.56))
    //    println("4.5 (Double)" -> n.convert(new lang.Double(4.5)))
    //    //    print("sadfsdf (String)" -> eai.convertTo("sadfsdf"))



    implicit val m: SolrDocumentMap = Map(
      "a" -> "",
      "b" -> 10
    )

    implicit val dc = new NoConversionCanConvertTo[Int]

    println("HZ: " + dc.convert("dsds"))

    //    implicit val c = DefaultConverters.DefaultBigIntConverter

    println("GWC: " + CanConvertTo.get[Int]("a"))



    //  final val NumberConverter = DefaultDoubleConverter ~ convertIntToDouble _

//    class A
//    class B extends A
//    class C
//
//    val ac = new Converter[A, Int] {
//      override def convert(from: A): Option[Int] = null
//    }
//
//    val bc = new Converter[B, Int] {
//      override def convert(from: B): Option[Int] = null
//    }
//
//    val cc = new Converter[C, Int] {
//      override def convert(from: C): Option[Int] = null
//    }
//
//    val ac1 = new Converter[A, A] {
//      override def convert(from: A): Option[A] = null
//    }
//
//    val bc1 = new Converter[B, B] {
//      override def convert(from: B): Option[B] = null
//    }
//
//    val abconv = ac ~ bc
//    val baconv = bc ~ ac
//    val acconv = ac ~ cc
//
//    val abconv1 = ac1 ~ bc1


    //  println(paramInfo(abconv))
    //  println(paramInfo(baconv))
    //  println(paramInfo(acconv))
    //
    //  println(paramInfo(abconv1))

    //  abconv.convert(new A)
    //  abconv.convert(new B)

    //  baconv.convert(new A)
    //  baconv.convert(new B)

    //  acconv.convert(new A)
    //  acconv.convert(new A)

    //  def paramInfo[T: TypeTag](x: T): Unit = {
    //    val targs = typeOf[T] match {
    //      case TypeRef(_, _, a) => a
    //      case v => v
    //    }
    //    println(s"type of $x has type arguments $targs")
    //  }


  }


}











//trait CanConvertMapToClass[Result] {
//  def convert(document: SolrDocumentMap): Result
//}
//
//
//
//trait CanConvertField[From, To] {
//  def canConvert()
//}



//
//object CanConvertMapToClass {
//
//  type SolrDocumentMap = Map[String, Any]
//
//
//  //  def get[T](field: String)
//  //            (implicit document: SolrDocumentMap, classTag: ClassTag[T]): T = {
//  //
//  //    document.get(field) match {
//  //      case None => noFieldException(field)
//  //      case Some(value: T) => value
//  //      case Some(e) => incorrectTypeException(field, e)
//  //    }
//  //  }
//  //
//  //  def get[T](field: String)
//  //            (implicit document: SolrDocumentMap, classTag: ClassTag[T], converter: CanConvertMapToClass[T]): T = {
//  //    document.get(field) match {
//  //      case None => noFieldException(field)
//  //      case Some(value: T) => value
//  //      case Some(e) => incorrectTypeException(field, e)
//  //    }
//  //  }
//  //
//  //
//  //  //  def get[@specialized T <: AnyVal](field: String, default: T)
//  //  def get[T](field: String, default: T)
//  //            (implicit document: SolrDocumentMap, classTag: ClassTag[T]): T = {
//  //
//  //    document.get(field) match {
//  //      case None => default
//  //      case Some(value: T) => value
//  //      case Some(e) => incorrectTypeException(field, e)
//  //    }
//  //  }
//
//  //  def get[T <: AnyRef](field: String, default: T)(implicit document: SolrDocumentMap, classTag: ClassTag[T]): T = {
//
//  //
//  //    document.get(field) match {
//  //      case None => default
//  //      case Some(value: T) => value
//  //      case Some(e) => incorrectTypeException(field, e)
//  //    }
//  //  }
//
//
//  //  def opt[T](field: String)(implicit document: SolrDocumentMap, classTag: ClassTag[T]): Option[T] = {
//  //
//  //    document.get(field) match {
//  //      case None => None
//  //      case Some(value: T) => Some(value)
//  //      case Some(e) => incorrectTypeException(field, e)
//  //    }
//  //  }
//  //
//  //  private def noFieldException(field: String): Nothing = {
//  //    throw new Exception(s"No required field '$field' in the document.")
//  //  }
//
//  //  private def incorrectTypeException[T](field: String, value: T)(implicit classTag: ClassTag[T]): Nothing = {
//  //    val resultClassName = classTag.runtimeClass.getSimpleName
//  //    val valueClassName = value.getClass.getSimpleName
//  //    throw new Exception(
//  //      s"Field '$field' must be instance of '$resultClassName', but its value is '$valueClassName'.")
//  //  }
//}
//
//
