package ars.solr.query

import java.util.Date

import ars.solr.AbstractBaseTest
import com.typesafe.scalalogging.Logger
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.client.solrj.{SolrRequest, SolrServer}
import org.apache.solr.common.SolrInputDocument
import org.apache.solr.common.params.ModifiableSolrParams

/**
  * Created by ars on 20/11/2016.
  */
@deprecated
class SolrLegacyQueryBuilderTest extends AbstractBaseTest {

//  import SolrLegacyQueryBuilder._
//
//  "Embedded Solr Server" must "ddd" in {
//
//    val server = createSolrServer
//    server.deleteByQuery("*:*")
//    server.commit()
//
//    val doc = new SolrInputDocument()
//    doc.addField("id", "id--11111" + new Date())
//    doc.addField("name", "name-name-name-333")
//
//    server.add(doc)
//    server.commit()
//
//
//    val query = "*:*"
//
//    val params = new ModifiableSolrParams()
//    params.set("q", query);
//
//    val results = server.query(params).getResults
//    val num = results.getNumFound
//    SolrLegacyQueryBuilderTest.logger.error("NUM: " + num)
//    if (num > 0) {
//      SolrLegacyQueryBuilderTest.logger.error("RES: " + results.get(0) )
//    }
//
//
//
//
//
//
//
////    createSolrServer.request(new Solr)
//
//    server.shutdown()
//  }
//
//  "OperatorQuery" must "validate arguments" in {
//
//    val name = "OP"
//    val queries = Seq(new FieldQuery("n", "v"), new FieldQuery("n1", "v1"))
//
//    val op = new OperatorQuery(name)(queries :_ *)
//    assert(op != null)
//    assert(op.name != null)
//    assertResult("OP")(op.name)
//    assert(op.args != null)
//    assertResult(2)(op.args.size)
//
//    val first = op.args(0)
//    val second = op.args(1)
//    assert(first.isInstanceOf[FieldQuery])
//    assert(second.isInstanceOf[FieldQuery])
//
//    val firstField = first.asInstanceOf[FieldQuery]
//    assertResult("n")(firstField.name)
//    assertResult("v")(firstField.string)
//
//    val secondField = second.asInstanceOf[FieldQuery]
//    assertResult("n1")(secondField.name)
//    assertResult("v1")(secondField.string)
//
//    intercept[IllegalArgumentException] {
//      new OperatorQuery(null)(queries :_ *)
//    }
//
//    intercept[IllegalArgumentException] {
//      new OperatorQuery("")(queries :_ *)
//    }
//
//    intercept[IllegalArgumentException] {
//      new OperatorQuery(name)(null)
//    }
//
//    intercept[IllegalArgumentException] {
//      new OperatorQuery(name)(Seq() :_ *)
//    }
//
//    intercept[IllegalArgumentException] {
//      new OperatorQuery(name)(Seq(null) :_ *)
//    }
//
//    intercept[IllegalArgumentException] {
//      val query: Seq[Query] = Seq(new FieldQuery("n", "v"), null)
//      new OperatorQuery(name)(query :_ *)
//    }
//
//    intercept[IllegalArgumentException] {
//      val query: Seq[Query] = Seq(null, new FieldQuery("n", "v"))
//      new OperatorQuery(name)(query :_ *)
//    }
//  }
//
//  it must "have toString returning valid Solr query" in {
//    val name = "OP"
//    val fieldQuery = new FieldQuery("n", "v")
//    val fieldQuery1 = new FieldQuery("n1", "v1")
//    val fieldQuery2 = new FieldQuery("n2", "v2")
//
//    assertResult(fieldQuery.toString)( new OperatorQuery(name)(Seq(fieldQuery) :_ *).toString)
//    assertResult(s"(${fieldQuery1.toString} $name ${fieldQuery2.toString})")(new OperatorQuery(name)(Seq(fieldQuery1, fieldQuery2) :_ *).toString)
//  }
//
//  "FieldQuery" must "validate arguments" in {
//    val name = "OP"
//    val pattern = "pattern"
//
//    val field = new FieldQuery(name, pattern)
//    assert(field != null)
//    assert(field.name == name)
//    assert(field.string == pattern)
//
//    intercept[IllegalArgumentException] {
//      new FieldQuery(null, pattern)
//    }
//
//    intercept[IllegalArgumentException] {
//      new FieldQuery(name, null)
//    }
//
//    intercept[IllegalArgumentException] {
//      new FieldQuery("", pattern)
//    }
//
//    intercept[IllegalArgumentException] {
//      new FieldQuery(name, "")
//    }
//
//    intercept[IllegalArgumentException] {
//      new FieldQuery(" ", pattern)
//    }
//
//    intercept[IllegalArgumentException] {
//      new FieldQuery(name, " ")
//    }
//  }
//
//  it must "have toString returning valid Solr query" in {
//    val name = "OP"
//    val pattern = "pattern"
//
//    assertResult(s"$name:$pattern")(new FieldQuery(name, pattern).toString)
//  }
//
//  "Complex query string" must "be valid" in {
//    val query =
//      new OperatorQuery("*") (
//        new OperatorQuery("-") (
//          new FieldQuery("n1", "v1"),
//          new FieldQuery("n2", "v2"),
//          new OperatorQuery("/") (
//            new FieldQuery("n3", "v3")
//          )
//        ),
//        new FieldQuery("n4", "v4")
//    )
//
//    val str = "((n1:v1 - n2:v2 - n3:v3) * n4:v4)"
//
//    assertResult(str)(query.toString)
//  }
//
//  "DSL" must "be valid" in {
//    query(
//      field("n", "v")
//    )
//
//    query(
//      and(
//        field("n", "v"),
//        field("n", "v")
//      )
//    )
//
//    query(
//      or(
//        field("n", "v"),
//        field("n", "v")
//      )
//    )
//
//    query(
//      operator("name", field("n", "v"))
//    )
//
//    query(
//      or(
//        field("n", "v"),
//        or(
//          field("n", "v"),
//          field("n", "v")
//        ),
//        field("n", "v")
//      )
//    )
//
//    query(
//      and(
//        field("n", "v"),
//        or (
//          field("n1", "v1"),
//          field("n1", "v1")
//        ),
//        field("n2", "v2"),
//        and (
//          field("n3", "n4"),
//          field("n1", "v1")
//        )
//      )
//    )
//
//    query {
//      field("n", "v")
//    }
//
//  }
}

@deprecated
object SolrLegacyQueryBuilderTest {
  def logger = Logger[SolrLegacyQueryBuilderTest]
}
