//package ars.solr.example
//
//import ars.solr.AbstractBaseTest
//import ars.solr.client.CanConvertMapToClass.{get, opt}
//import ars.solr.client.SolrDocumentMap
//import ars.solr.config.SolrCoreConfig
//import ars.solr.pool.SolrConnectionPoolFactory.createScalaPool
//import ars.solr.pool.SolrExecutor
//import ars.solr.query.SolrBasicQueryFactory.field
//import ars.solr.query._
//import ars.solr.client.implicits.{CanConvertMapToClass, _}
//
///**
//  * Example.
//  *
//  * @author ars (Ibragimov Arsen)
//  * @since 0.0.5
//  */
//class ExampleTest extends AbstractBaseTest {
//
//  // create class
//  case class MyClass(firstName: String, lastName: String,
//                     bookTitle: String, year: Int, rating: Option[Int])
//
//  // write conversion function
//  implicit val toMyClass: CanConvertMapToClass[MyClass] =
//    (document: SolrDocumentMap) => {
//      implicit val doc = document
//
//      MyClass(
//        get[String]("first_name"),
//        get[String]("last_name"),
//        get[String]("book_title"),
//        get[Int]("year", 1900),
//        opt[Int]("rating")
//      )
//    }
//
//
//  // TODO Add core
//  def readMeExample(): Unit = {
//    val coreConfig = SolrCoreConfig("http:/.../solr/", "core-name")
//
//    val maxConnections = 100
//    val pool = createScalaPool(coreConfig, maxConnections)
//
//    val executor = new SolrExecutor(pool)
//
//    val booksWithRating = "book_title" --> ("The", "name") AND field("rating")
//    val author = ("first_name" --> "Mark") * 1.5F AND "last_name" -*> "Tw"
//    val query = booksWithRating OR author
//
//    executor { client =>
//      val result = client
//        .buildQuery(query)
//        .fields("first_name", "last_name", "book_title", "year", "rating") // optional
//        .offset(10) // optional
//        .limit(25) // optional
//        .execute()
//
//      // native map result documents
//      val nativeDocuments = result.documents
//
//      val documents = result.documents[MyClass]
//
//    }
//  }
//}
