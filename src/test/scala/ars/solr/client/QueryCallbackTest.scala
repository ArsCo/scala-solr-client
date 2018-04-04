package ars.solr.client

import java.lang.{Float => JFloat}

import ars.solr.AbstractBaseTest
import ars.solr.client.QueryCallback.{NoopDocumentCall, NoopInfoCall}
import org.apache.solr.client.solrj.StreamingResponseCallback
import org.apache.solr.common.SolrDocument

/** Tests for [[QueryCallback]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class QueryCallbackTest extends AbstractBaseTest {
  "toNativeCallback" must "convert QueryCallback to StreamingResponseCallback" in {
    val qc = QueryCallback()
    val s = qc.toNativeCallback
    assert(s != null)
    assert(s.isInstanceOf[StreamingResponseCallback])
  }

  val docCall = (d: SolrDocument) => {}
  val infoCall = (num: Long, start: Long, maxScore: Option[Float]) => {}
  val src = new StreamingResponseCallback {
    override def streamSolrDocument(doc: SolrDocument) = {}
    override def streamDocListInfo(numFound: Long, start: Long, maxScore: JFloat) = {}
  }

  "apply(DocumentCall, NoopInfoCall)" must "validate args" in {
    intercept[IllegalArgumentException] {
      QueryCallback(null, infoCall)
    }

    intercept[IllegalArgumentException] {
      QueryCallback(docCall, null)
    }
  }

  it must "have valid default args" in {
    val qc = QueryCallback()
    assertResult(NoopDocumentCall)(qc.document)
    assertResult(NoopInfoCall)(qc.info)
  }

  it must "create new instance" in {
    val qc = QueryCallback(docCall, infoCall)
    assertResult(docCall)(qc.document)
    assertResult(infoCall)(qc.info)
  }

  "apply(DocumentCall)" must "validate args" in {
    val qc = QueryCallback(docCall)
    assertResult(docCall)(qc.document)
    assertResult(NoopInfoCall)(qc.info)
  }

  "apply(InfoCall)" must "validate args" in {
    val qc = QueryCallback(infoCall)
    assertResult(NoopDocumentCall)(qc.document)
    assertResult(infoCall)(qc.info)
  }

  "fromNativeCallback" must "validate arg" in {
    intercept[IllegalArgumentException] {
      QueryCallback.fromNativeCallback(null)
    }
  }

  it must "unwrap StreamingResponseCallbackWrapper" in {
    val qc = QueryCallback(docCall)
    val s = new StreamingResponseCallbackWrapper(qc)
    val r = QueryCallback.fromNativeCallback(s)
    assertResult(qc)(r)
  }

  it must "wrap StreamingResponseCallback" in {
    val r = QueryCallback.fromNativeCallback(src)
    assert(r != null)
    assert(r.isInstanceOf[QueryCallback])
  }


  "toNativeCallback" must "validate arg" in {
    intercept[IllegalArgumentException] {
      QueryCallback.toNativeCallback(null)
    }
  }

  it must "unwrap QueryCallbackWrapper" in {
    val qcw = new QueryCallbackWrapper(src)
    val r = QueryCallback.toNativeCallback(qcw)
    assertResult(src)(r)
  }

  it must "wrap QueryCallback" in {
    val q = QueryCallback()
    val r = QueryCallback.toNativeCallback(q)
    assert(r != null)
    assert(r.isInstanceOf[StreamingResponseCallback])
  }
}
