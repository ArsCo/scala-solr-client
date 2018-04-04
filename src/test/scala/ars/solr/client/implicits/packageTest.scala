//package ars.solr.client.implicits
//
//import java.lang.{Float => JFloat}
//
//import ars.solr.AbstractBaseTest
//import ars.solr.client._
//import ars.solr.client.QueryCallback.{DocumentCall, InfoCall, NoopDocumentCall, NoopInfoCall}
//import org.apache.solr.client.solrj.StreamingResponseCallback
//import org.apache.solr.common.SolrDocument
//
///**
//  * Tests for [[ars.solr.client.implicits]].
//  *
//  * @author ars (Ibragimov Arsen)
//  * @since 0.0.5
//  */
//class packageTest extends AbstractBaseTest {
//
//  import ars.solr.client.implicits._
//
//  "docToQueryCallback" must "convert (implicitly) DocumentCall to QueryCallback" in {
//    val docCall = (s: SolrDocument) => {}
//    val qc: QueryCallback = docCall
//    assert(qc != null)
//    assertResult(docCall)(qc.document)
//    assertResult(NoopInfoCall)(qc.info)
//  }
//
//  "infoToQueryCallback" must "convert (implicitly) InfoCall to QueryCallback" in {
//    val infoCall: InfoCall = (l1: Long, l2: Long, f: Option[Float]) => {}
//    val qc: QueryCallback = infoCall
//    assert(qc != null)
//    assertResult(NoopDocumentCall)(qc.document)
//    assertResult(infoCall)(qc.info)
//  }
//
//  "nativeCallbackToQueryCallback" must "convert (implicitly) StreamingResponseCallback to QueryCallback" in {
//    val src = new StreamingResponseCallback {
//      override def streamSolrDocument(doc: SolrDocument) = {}
//      override def streamDocListInfo(numFound: Long, start: Long, maxScore: JFloat) = {}
//    }
//
//    val qc: QueryCallback = src
//    assert(qc != null)
//    qc match {
//      case w: QueryCallbackWrapper => assertResult(src)(w.nativeCallback)
//      case _ => fail()
//    }
//  }
//  "queryCallbackToNativeCallback" must "convert (implicitly) QueryCallback to StreamingResponseCallback" in {
//    val qc = new QueryCallback {
//      override def document: DocumentCall = NoopDocumentCall
//      override def info: InfoCall = NoopInfoCall
//    }
//
//    val src: StreamingResponseCallback = qc
//    assert(src != null)
//    src match {
//      case w: StreamingResponseCallbackWrapper => assertResult(qc)(w.callback)
//      case _ => fail()
//    }
//  }
//
//  "toCanConvertMapToClass" must "convert (implicitly) function to CanConvertMapToClass" in {
//
//    val f = (d: SolrDocumentMap) => {5}
//    val c: CanConvertMapToClass[Int] = f
//    assert(c != null)
//  }
//}
