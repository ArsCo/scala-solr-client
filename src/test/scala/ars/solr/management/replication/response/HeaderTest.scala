package ars.solr.management.replication.response

import ars.solr.AbstractBaseTest
import ars.solr.exception.SolrException
import org.json4s.JsonDSL._


/** Tests for [[Header]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
class HeaderTest extends AbstractBaseTest {

  "Header" must "have valid arg types" in {
    Header(123L, 345L)
  }

  "Header.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      Header.fromJson(null)
    }
  }

  val status = 0L
  val errorStatus = 10L
  val qTime = 34567L

  it must "parse valid JSON" in {

    val v = ("responseHeader" ->
              ("status" -> status) ~
              ("QTime" -> qTime))

    assertResult(Header(status, qTime)) {
      Header.fromJson(v)
    }
  }

  it must "throw SolrException if status != 0" in {
    val v = ("responseHeader" ->
              ("status" -> errorStatus) ~
              ("QTime" -> qTime))

    intercept[SolrException] {
      Header.fromJson(v)
    }
  }
}
