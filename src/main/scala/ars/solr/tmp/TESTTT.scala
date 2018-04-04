//def example(): Unit = {
//  val c = new ScalaSolrClient(null)
//  val d1 = new SolrInputDocument()
//  val d2 = new SolrInputDocument()
//  val d3 = new SolrInputDocument()
//
//  c.add()
//  c.add(d1)
//  c.add(d1, d2)
//  c.add(d1, d2, d3)
//
//  c.add(5)
//  c.add(5, d1)
//  c.add(5, d1, d2)
//  c.add(5, d1, d2, d3)
//
//  c.add(Seq() :_ *)
//  c.add(Seq(d1, d2, d3) :_ *)
//  c.add(5, Seq(d1, d2, d3) :_ *)
//
//  c.add("collection", d1)
//  c.add("collection", d1, d2)
//  c.add("collection", d1, d2, d3)
//
//  c.add("collection", 5, d1)
//  c.add("collection", 5, d1, d2)
//  c.add("collection", 5, d1, d2, d3)
//
//}

//
//def example(): Unit = {
//  val c = new ScalaSolrClient(null)
//  val id1 = "111"
//  val id2 = "222"
//  val id3 = "333"
//
//  val d1 = new SolrInputDocument()
//  val d2 = new SolrInputDocument()
//  val d3 = new SolrInputDocument()
//
//  val collection = Collection("collection")
//
//  c.add()
//  c.add(d1)
//  c.add(d1, d2)
//  c.add(d1, d2, d3)
//
//  c.add(5)
//  c.add(5, d1)
//  c.add(5, d1, d2)
//  c.add(5, d1, d2, d3)
//
//  c.add(Seq() :_ *)
//  c.add(Seq(d1, d2, d3) :_ *)
//  c.add(5, Seq(d1, d2, d3) :_ *)
//
//
//  c.add(collection, d1)
//  c.add(collection, d1, d2)
//  c.add(collection, d1, d2, d3)
//
//  c.add(collection, 5, d1)
//  c.add(collection, 5, d1, d2)
//  c.add(collection, 5, d1, d2, d3)
//
//  c.deleteById()
//  c.deleteById(id1)
//  c.deleteById(id1, id2)
//  c.deleteById(id1, id2, id3)
//
//  c.deleteById(5)
//  c.deleteById(5, id1)
//  c.deleteById(5, id1, id2)
//  c.deleteById(5, id1, id2, id3)
//
//  c.deleteById(Seq() :_ *)
//  c.deleteById(Seq(id1, id2, id3) :_ *)
//  c.deleteById(5, Seq(id1, id2, id3) :_ *)
//
//  c.deleteById(collection, id1)
//  c.deleteById(collection, id1, id2)
//  c.deleteById(collection, id1, id2, id3)
//
//  c.deleteById(collection, 5, id1)
//  c.deleteById(collection, 5, id1, id2)
//  c.deleteById(collection, 5, id1, id2, id3)
//
//}

//import ars.solr.client.MappingErrorMode
//package ars.solr.tmp
//
//import java.io.PrintWriter
//import java.util.Date
//import java.util.concurrent.Executors
//
//import ars.solr.client._
//import ars.solr.client.implicits._
//import CanConvertMapToClass
//import ars.solr.query.implicits._
//import ars.solr.client.request.RequestBuilder
//import ars.solr.config.SolrCoreConfig
//import ars.solr.pool.SolrConnectionPoolFactory.createScalaPool
//import ars.solr.pool.SolrExecutor
//import ars.solr.query.SolrBasicQueryFactory.allDocs
//import com.typesafe.scalalogging.Logger
//import org.apache.solr.common.SolrDocument
//
//import scala.annotation.StaticAnnotation
//import scala.io.Source
//
///**
//  * Created by ars on 06/03/2017.
//  */
//class TESTTT {
////  implicit val toMyClass = new CanConvertMapToCaseClass[MyCaseClass] {
////    override def convert(document: SolrDocument): MyCaseClass = {
////      import CanConvertMapToCaseClass._
////      MyCaseClass(
////        get[String]("full_name"),
////        get[Int]("my_value"),
////        optGet[Date]("end_date")
////      )
////    }
////  }
//
////  def clientSyntaxTest(): Unit = {
////    val core = SolrCoreConfig("server", "coreName")
////    val client = ScalaSolrClient.createDefaultHttpClient(core)
////
////    val query = "ddd" --> "ddd"
////
////
////    val solrQuery = new QueryBuilder(
////      query = query,
////      _start = Some(0),
////      _rows = Some(20)).toSolrParams
////
////    val solrQuery2 = new QueryBuilder(query).start(0).rows(10).toSolrParams
////
////    client.query(solrQuery).nativeResponse
////
////    client.query(solrQuery2)
////
////
////    val myClassValues: Seq[MyCaseClass] = client.query(solrQuery).resultAsCaseClasses
////
////
////
////
////    //client.query()
////  }
//
//
//  def createExecutor(): SolrExecutor = {
//    val c = SolrCoreConfig("http://hbs-solrm01-prod:8983/solr/", "detmir-fias")
//
//    new SolrExecutor(createScalaPool(c, 100))
//  }
//
//  def run(executor: SolrExecutor): Unit = {
////    import implicits._
//
//
//    val query = allDocs
//
//    executor { client =>
//
//      client.buildQuery(allDocs)
//        .limit(100000).stream().fixedThreadPool(5)
//        .execute ( (d: SolrDocument) => {
//          val th = Thread.currentThread().getName
//          println(th + "---" + d)
//        }
//
//          //        .executeStream(
//          //        )
//
//
//          //      if (result.numFound > 0) {
//          //        result.documents.foreach(println)
//          //      }
//
//        )
//    }
//  }
//
//  def r = {
//    run(createExecutor())
//  }
//
//  implicit val toFiasData: CanConvertMapToClass[FiasData] = new CanConvertMapToClass[FiasData] {
//    override def convert(document: SolrDocumentMap): FiasData = {
//      import CanConvertMapToClass._
//
//      implicit val v: SolrDocumentMap = document.asInstanceOf[SolrDocumentMap]
//
//      FiasData(
//        get[String]("AOGUID"),
//        opt[String]("PARENTGUID"),
//        get[String]("CODE"),
//        get[String]("OFFNAME"),
//        opt[String]("hierarchy_name"),
//        get[String]("SHORTNAME"),
//        get[String]("full_name")
//      )
//    }
//  }
//
//  def parseStreet(street: String): String = {
//    var s = street.replaceAll("\\s+", " ").trim
//
//    if (s.contains("(")) {
//      s = s.substring(0, s.indexOf("(")).trim
//    }
//
//    s = repreg(s)
//
//    if (s.toLowerCase().contains("нет")) {
//      "NO"
//    } else {
//      s
//    }
//
//    val ss = s.split("\\s+", -1)
//    if (ss.length > 1) {
//      ss.filter(v => !(v.length < 4)).mkString(" ")
//
//    } else {
//      s
//    }
//  }
//
//  def repreg(s: String): String = {
//    val Reg = "([\\d]+-[а-я]+)\\s+(.*)".r
//
//    val m = s match {
//      case Reg(num, bs) => s"$bs $num"
//      case _ => s
//    }
//
//    m.replaceAll("[\\(\\)]+", "")
//  }
//
//  def fooFile(path: String, outPath: String, outDouble: String): Unit = {
//    val pairs = for{
//      line  <- Source.fromFile(path).getLines().toSeq
//      lineArr = line.split(";", -1)
//      v = (lineArr(0).trim, lineArr(1).trim)
//    } yield v
//
//    foo(pairs, outPath, outDouble)
//  }
//
//  // City ; Street; shortName
//  def foo(streets: Seq[(String, String)], outPath: String, outDouble: String): Unit = {
//
//    val core = SolrCoreConfig("http://hbs-solrm01-prod:8983/solr/", "detmir-fias")
//    val client = ScalaSolrClient.createClient(core)
//
//    val pw = new PrintWriter(outPath)
//    val dw = new PrintWriter(outDouble)
//    try {
//
//      streets foreach { case (rest, street) =>
//
//        try {
//          val restStr = if (rest.length > 6) rest.substring(0, 5) else rest
//
//          val street1 = parseStreet(street)
//
//          val q = ("CODE" --> "50*" OR "CODE" --> "77*") AND
//            "OFFNAME" *~> s"*$street1*" AND "AOLEVEL" --> "7"
//
//          logger.error(s"ST: '$street' -> '$street1' REST: '$rest' -> '$restStr'")
//
//          val fd = client.buildQuery(q).offset(0).limit(150).execute().documents[FiasData]
//
//          val unfil = fd.map { f =>
//            f.hierarchyName match {
//              case None =>
//                //logger.error("NO: " + f.fullName)
//                val hhn = hn(client, f.parentGuid)
//                //logger.error("HHN: " + hhn)
//                f.copy(hierarchyName = Some(hhn))
//              case _ => f
//
//            }
//          }
//
//          if (unfil.isEmpty) {
//            val str = s"${rest};$street;-->\n"
//            pw.append(str)
//
//          } else {
//
//            val filt = unfil.filter { v =>
//
//
//              val res = v.hierarchyName.get.contains(restStr)
//              //logger.error(s"H: ${v.hierarchyName} -> $restStr" )
//              res
//
//            }
//
//            val res = if (filt.isEmpty) unfil else filt
//
//            if (cont77And50(res)) {
//              res.foreach { v =>
//                val str = s"${rest};$street;-->;${v.code};${v.shortName};${v.offname};${v.hierarchyName.get}\n"
//                dw.append(str)
//                dw.flush()
//              }
//            } else {
//              res.foreach { v =>
//                val str = s"${rest};$street;-->;${v.code};${v.shortName};${v.offname};${v.hierarchyName.get}\n"
//                pw.append(str)
//                pw.flush()
//              }
//            }
//          }
//        } catch {
//          case e: Throwable => logger.error("" + e)
//        }
//      }
//    } finally {
//      pw.close()
//      dw.close()
//    }
//  }
//
//  def cont77And50(fd: Seq[FiasData]): Boolean = {
//    val c50 = fd.exists(v => v.code.startsWith("50"))
//    val c77 = fd.exists(v => v.code.startsWith("77"))
//    c50 && c77
//  }
//
//  def hn(client: ScalaSolrClient, parentGuid: Option[String]): String = {
//    parentGuid match {
//      case None => "[]"
//      case Some(pg) =>
//        val q = "AOGUID" --> pg
//        //logger.error("REQ: " + q)
//        val fd = client.buildQuery(q).offset(0).limit(1).execute().documents[FiasData]
//        //logger.error("RES:" + fd.head)
//
//       val r = (fd.head.fullName + " -> " + hn(client, fd.head.parentGuid))
//        //logger.error("RET: " + r)
//        r
//    }
//  }
//
//  def logger = Logger[TESTTT]
//
//
//  def bbb(): Unit = {
//    val rb = RequestBuilder("dfdf")(null)
//
//    rb
//      .fields()
//      .defaultFields()
//
//      .offset(5)
//      .defaultOffset()
//
//      .limit(5)
//      .defaultLimit()
//
//      .sorts()
//      .defaultSorts()
//
//      .handler("")
//      .selectHandler()
//
//      .timeAllowed(555)
//      .defaultTimeAllowed()
//
//      .filterQuery()
//      .defaultFilterQuery()
//
//      .set("", "")
//      .set("", true)
//      .remove("")
//
//      .includeScore(true)
//      .showDebug(true)
//
//      .highlights(
//        _
//          .fields("")
//          .fragmentSize(5)
//          .simplePost("вава")
//      )
//
//      .facets { f =>
//        f.limit(5)
//        .missing(true)
//        .fields()
//        .prefix("")
//      }
//
//      .execute()
//
//    rb
//      .stream()
//      .singleThreadPool()
//      .cachedThreadPool()
//      .fixedThreadPool(5)
//      .threadPool(Executors.newCachedThreadPool())
//      .execute(QueryCallback())
//
//
//    rb
//      .facets(_
//        .fields("")
//        .limit(5)
//        .minCount(5)
//        .pivot(""))
//  }
//
//
//
//
//  //  class DefaultCanConvertMapToCaseClass[T](implicit classTag: ClassTag[T]) extends CanConvertMapToCaseClass[T] {
//  //    override def convert(document: SolrDocument): T = {
//  //      val solrF = classTag.runtimeClass.getAnnotation(classOf[solrField])
//  //      solrF.name
//  //
//  //
//  //    }
//  //  }
//
//}
//
//case class MyCaseClass(name: String, value: Int, date: Option[Date])
//
//case class MyAnnotatedClass(
//                             @solrField("n") name: String,
//                             @solrField("v") value: Int,
//                             @solrField("d") date: Option[Date])
//
//
//case class solrField(name: String) extends StaticAnnotation {
//
//}
//
//
//case class FiasData(
//
// guid: String,
// parentGuid: Option[String],
// code: String,
// offname: String,
// hierarchyName: Option[String],
// shortName: String,
// fullName: String
//)
//



//  override def documents[Result](errorMode: MappingErrorMode)(implicit mapper: CanMapTo[Result]): Seq[Result] = {
//    val predicate = errorModeToPredicate[Result](errorMode)
//    documents[Result](predicate)
//  }


//sealed abstract class MappingErrorMode
//object MappingErrorModes {
//
//  /** Discards all documents that can't be mapped. */
//  case object DiscardFailed extends MappingErrorMode
//
//  /** Aborts mapping if there's at least one document that can't be mapped. */
//  case object Abort extends MappingErrorMode
//}

//  /**
//    * Tries to get result documents as instances of `Result` type. This method uses `mapper` to
//    * map [[SolrDocumentMap]] to `Result` type. The processing of documents that can't mapped to
//    * `Result` depends on `errorMode`.
//    *
//    * If `errorMode` is [[ars.solr.client.MappingErrorModes.Abort]] and there's at least one
//    * document that can't be mapped to `Result` then [[ars.solr.client.mapping.SolrMappingException]]
//    * will be thrown.
//    *
//    * If `errorMode` is [[ars.solr.client.MappingErrorModes.DiscardFailed]] then all documents that can't
//    * be mapped to `Result` will be discarded from the resulting sequence.
//    *
//    * @param mapper the mapper
//    *
//    * @tparam Result the result type
//    *
//    * @return the sequence of converted result documents
//    */
//  def documents[Result](errorMode: MappingErrorMode = Abort)(implicit mapper: CanMapTo[Result]): Seq[Result]


//  private def errorModeToPredicate[Result](errorMode: MappingErrorMode): Predicate[Result] = {
//
//
//
//    errorMode match {
//      case Abort => actionPredicate(_, )
//      case DiscardFailed => actionPredicate(_, )
//      case _ => throw new IllegalArgumentException(s"Unknown error mode: $errorMode") // Abnormal case
//    }
//  }


package ars.solr.client.conversion.converters

import ars.precondition.RequireUtils
import ars.precondition.RequireUtils.{requireNotBlank, requireNotNull}
import ars.solr.client.{Collection, ScalaSolrClient}
import org.apache.solr.common.SolrInputDocument


trait FromConverter[From, To]

/**
  *
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.0
  */
object OtherConverters {

  //
  //  final implicit val DefaultMoneyConverter = new CanConvertWithSequenceTo(convertToMoney _)







  // TODO: param values

  private def convertToMoney(from: Any): Option[Money] = {
    from match {
      case v: String => parseMoneyFromString(v)
      case _ => None
    }
  }

  protected def parseMoneyFromString(value: String): Option[Money] = {
    val parts = value.split(',')
    if (parts.length != 2) None
    else {
      try {
        val amount = BigDecimal(parts(0))
        val code = parts(1)
        Some(Money(amount, code))

      } catch {
        case e: NumberFormatException => // TODO: Add logging
          None
      }
    }
  }

  def toValue(money: Money): String = {
    Money.toValue(money)
  }


  //  private def incorrectTypeException[T](field: String, value: T)(implicit classTag: ClassTag[T]): SolrException = {
  //    val resultClassName = classTag.runtimeClass.getCanonicalName
  //    val valueClassName = value.getClass.getCanonicalName
  //    new SolrException()(s"Field '$field' must be instance of '$resultClassName', but '$valueClassName'.")
  //  }

}

// ---------- CanConvertMapToClass ----------

//  /** Implicit conversion from converter to [[CanConvertMapToClass]]. */
//  implicit def toCanConvertMapToClass[Result](converter: (SolrDocumentMap) => Result): CanConvertMapToClass[Result] = {
//    new CanConvertMapToClass[Result] {
//      override def convert(document: SolrDocumentMap): Result = converter(document)
//    }
//  }


case class Money(amount: BigDecimal, currency: String) {
  requireNotNull(amount, "amount")
  requireNotBlank(currency, "currency")
}

object Money {
  def toValue(money: Money): String = {
    val v = money.amount.setScale(2).toString()
    val cur = money.currency
    s"$v,$cur"
  }
}



abstract sealed class Currency(val value: String)
object Currencies {
  private final val AllCurrencies = Map(
    RUB.value -> RUB,
    USD.value -> USD
  )

  final case object RUB extends Currency("RUB")
  final case object USD extends Currency("USD")

  final case class CustomCurrency(override val value: String) extends Currency(value)

  def apply(value: String): Currency = CustomCurrency(value)
}
