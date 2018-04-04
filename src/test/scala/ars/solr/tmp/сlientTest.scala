package ars.solr.tmp

/**
  * Created by ars on 06/03/2017.
  */
class сlientTest {

//  implicit val toMyClass = new CanConvertMapToCaseClass[MyCaseClass] {
//    override def convert(document: SolrDocument): MyCaseClass = {
//      import CanConvertMapToCaseClass._
//      MyCaseClass(
//        get[String]("full_name"),
//        get[Int]("my_value"),
//        optGet[Date]("end_date")
//      )
//    }
//  }
//
//  def clientSyntaxTest(): Unit = {
//    val core = SolrCoreConfig("server", "coreName")
//    val client = ScalaSolrClient.createDefaultHttpClient(core)
//
//    val query = "ddd" --> "ddd"
//
//
//    val solrQuery = new SolrRequestBuilder(
//      query = query,
//      _start = Some(0),
//      _rows = Some(20)).toSolrParams
//
//    val solrQuery2 = new SolrRequestBuilder(query).start(0).rows(10).toSolrParams
//
//    client.query(solrQuery).nativeResponse
//
//    client.query(solrQuery2)
//
//
//    val myClassValues: Seq[MyCaseClass] = client.query(solrQuery).resultAsCaseClasses
//
//
//
//
//    //client.query()
//  }
//
//
////  def foo(): Seq[FiasData] = {
////    implicit val toFiasData = new CanConvertMapToCaseClass[FiasData] {
////      override def convert(document: SolrDocument): FiasData = {
////        import CanConvertMapToCaseClass._
////
////
////        FiasData(
////          get[String]("AOGUID"),
////          get[String]("PARENTGUID"),
////          get[String]("CODE"),
////          get[String]("OFFNAME"),
////          optGet[String]("hierarchy_name"),
////          get[String]("SHORTNAME")
////        )
////      }
////    }
////
////    val core = SolrCoreConfig("http://hbs-solrm01-prod:8983/solr/", "detmir-fias")
////    val client = ScalaSolrClient.createDefaultHttpClient(core)
////
////    val q = "AOGUID" --> "095ea835-be76-424b-858e-f854dbd6781b"
////
////    val sq = new SolrRequestBuilder(q).start(0).rows(10).toSolrParams
////
////
////    client.query(sq).resultAsCaseClasses
////  }
//
//
//
//
//
////  class DefaultCanConvertMapToCaseClass[T](implicit classTag: ClassTag[T]) extends CanConvertMapToCaseClass[T] {
////    override def convert(document: SolrDocument): T = {
////      val solrF = classTag.runtimeClass.getAnnotation(classOf[solrField])
////      solrF.name
////
////
////    }
////  }

}

//case class MyCaseClass(name: String, value: Int, date: Option[Date])
//
//case class MyAnnotatedClass(
//                             @tmp.solrField("n") name: String,
//                             @tmp.solrField("v") value: Int,
//                             @tmp.solrField("d") date: Option[Date])
//
//
//case class solrField(name: String) extends StaticAnnotation {
//
//}
//
//
//case class FiasData(
//                   guid: String,
//                   parentGuid: String,
//                   code: String,
//                   offname: String,
//                   hierarchyName: Option[String],
//                   shortName: String
//                   )
